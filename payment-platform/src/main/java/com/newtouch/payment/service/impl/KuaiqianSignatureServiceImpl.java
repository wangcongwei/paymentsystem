package com.newtouch.payment.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.newtouch.payment.exception.ServiceException;
import com.newtouch.payment.service.SignatureService;

/**
 * 
 * @author guanglei.ren
 * @version 1.0
 * @date 2018/03/12
 */
public class KuaiqianSignatureServiceImpl implements SignatureService {
	// 私钥证书路径
	private String ksfile;
	// 公钥证书路径
	private String certfile;
	// 私钥证书密码
	private String ksLoadPass;

	public String getKsfile() {
		return ksfile;
	}

	public void setKsfile(String ksfile) {
		this.ksfile = ksfile;
	}

	public String getCertfile() {
		return certfile;
	}

	public void setCertfile(String certfile) {
		this.certfile = certfile;
	}

	public String getKsLoadPass() {
		return ksLoadPass;
	}

	public void setKsLoadPass(String ksLoadPass) {
		this.ksLoadPass = ksLoadPass;
	}

	// 公钥证书密码
	private String provider = "BC";
	private String algorithm = "SHA1withRSA";
	private KeyStore keyStore;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private Resource privateResource; // 私钥资源
	private Resource publicResource; // 公钥资源

	private void initKeyStore() {
		InputStream in = null;
		if (Security.getProvider(provider) == null) {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		}
		try {
			if (keyStore == null) {
				keyStore = KeyStore.getInstance("PKCS12");
				if (privateResource == null) {
					privateResource = new ClassPathResource(ksfile);
				}
				if (publicResource == null) {
					publicResource = new ClassPathResource(certfile);
				}
				in = new FileInputStream(privateResource.getFile());
				keyStore.load(in, ksLoadPass.toCharArray());
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private PrivateKey getPrivateKey() {
		if (privateKey == null) {
			try {
				if (keyStore == null) {
					initKeyStore();
				}
//				String alias = keyStore.aliases().nextElement();
//				privateKey = (PrivateKey) keyStore.getKey(alias, ksLoadPass.toCharArray());
				privateKey = (PrivateKey) keyStore.getKey("test-alias", ksLoadPass.toCharArray());
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		return privateKey;
	}

	private PublicKey getPublicKey() {
		if (publicKey == null) {
			InputStream in = null;
			try {
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
				in = new FileInputStream(publicResource.getFile());
				X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(in);
				publicKey = certificate.getPublicKey();
			} catch (Exception e) {
				throw new ServiceException(e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return publicKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ccic.eb.service.onlinepayment.SignatureService#signature(byte[])
	 */
	@Override
	public String signature(byte[] dataToSign) {
		String signMsg = null;
		try {
			// 获取私钥
			PrivateKey privateKey = getPrivateKey();
//			ISignature signature = SignatureFactory.createSingature(algorithm);
			Signature signature = Signature.getInstance(algorithm);
			// 签名
			signature.initSign(privateKey);
			signature.update(dataToSign);
//			byte[] signedData = signature.sign(dataToSign, privateKey);
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			signMsg = encoder.encode(signature.sign());
//			signMsg = new String(Base64.encodeBase64(signedData));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return signMsg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ccic.eb.service.onlinepayment.SignatureService#verifySign(byte[],
	 * byte[])
	 */
	@Override
	public boolean verifySign(byte[] dataToSign, byte[] signedData) {
		boolean flag = false;
		// 获取公钥
		try {
			PublicKey publicKey = getPublicKey();
//			ISignature signature = SignatureFactory.createSingature(algorithm);
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(dataToSign);
			// 验签
//			flag = signature.verifySign(signedData, dataToSign, publicKey);
			flag=signature.verify(signedData);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return flag;
	}

}
