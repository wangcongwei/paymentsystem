/**
 * form 插件，支持校验，支持form数据解析拼装等
 * auther:zhangdongfeng
 */

define(function (require, exports, module) {
  "use strict";
  /**
 * 
 * @param value
 *            校验值
 * @param type
 *            类型
 * @param requird
 *            是否为空 true or false
 * @param minLen
 *            最小值
 * @param maxLen
 *            最大值
 * @param rangeStart
 *            开始
 * @param rangeEnd
 *            结束
 * @param pattern
 *            自定义正则表达式
 * @returns
 */
  var valiField = function (value, type, requird, minLen, maxLen, rangeStart, rangeEnd,
    pattern) {
    var pass = true;
    pass = pass && checkRequird(value, requird) && checkType(value, type)
    && checkLen(value, minLen, maxLen)
    && checkRange(value, type, rangeStart, rangeEnd)
    && checkPattern(value, buildPattern(type, pattern));
    return pass;
  }
  /**
 * 校验是否为空
 */
  var checkRequird = function (value, requird) {
    if (requird) {
      if (value != null && value != "") {
        return true;
      } else {
        return false;
      }
    }
    return true;
  }
  /**
 * 是否在此范围
 */
  var checkRange = function (value, type, rangeStart, rangeEnd) {
    if (rangeStart && rangeEnd && value) {
      if (value >= rangeStart && value <= rangeEnd) {
        return true;
      } else {
        return false;
      }
    }
    return true;
  }
  /**
 * 正则表达式校验
 */
  var checkPattern = function (value, pattern) {
    if (pattern != "" && pattern != null && value != null && value != "") {
      var re = new RegExp(pattern);
      return re.test(value);
    }
    return true;
  }
  /**
 * 校验字符长度
 */
  var checkLen = function (value, i, j) {
    if (value != "" && value != null) {
      if (value.length > i && value.length < j) {
        return true;
      } else {
        return false;
      }
    }
    return true;
  }

  /**
 * 校验是否能进行类型转换
 */
  var checkType = function (value, type) {
    try {
      if (type == "int" || type == "long" || type == "integer"|| type == "number") {
        if (!isNaN(parseInt(value)) && value == parseInt(value)) {
          return true;
        } else {
          return false;
        }
      }
      if (type == "float") {
        if (!isNaN(parseFloat(value)) && value == parseFloat(value)) {
          return true;
        } else {
          return false;
        }
      }
      if (type == "date" || type == "time" || type == "datetime") {
        if (!isNaN(new Date(value).getDate())) {
          return true;
        } else {
          return false;
        }
      }
    } catch (e) {
      return false;
    }

    return true;

  }
  /**
 * 正则
 */
  var buildPattern = function (type, pattern) {
    if (pattern != null && pattern != "") {
      return pattern;
    }
    if ("email" == type) {
      return "^([a-z0-ArrayA-Z]+[-|\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\.)+[a-zA-Z]{2,}$";
    } else if ("url" == type) {
      return "^(https?|ftp|file|https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    } else if ("pin" == type) {
      return "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9xX])";
    } else if ("mobile-number" == type) {
      return "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    } else if ("phone-number" == type) {
      return "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
    } else if ("integer" == type) {
      return "^\\+{0,1}[1-9]\\d";
    } else if ("float" == type) {
      return "^((0)|([1-9][0-9]*))\\.[0-9]+$";
    } else if ("float-2" == type) {
      return "^((0)|([1-9][0-9]*))\\.[0-9]{2}";
    } else if ("float-4" == type) {
      return "^((0)|([1-9][0-9]*))\\.[0-9]{4}";
    } else if ("date" == type) {
      return "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    } else if ("time" == type) {
      return "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    } else if ("datetime" == type) {
      return "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    } else if ("ip" == type) {
      return "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
    } else if ("letter" == type) {
      return "^[a-zA-Z_\\-0-9]+$";
    } else if ("uppercase" == type) {
      return "^[A-Z]+$";
    } else if ("lowercase" == type) {
      return "^[a-z]+$";
    } else {
      return "";
    }
  }

  var vali = function (formData, rules) {
    if (!formData || !rules || !rules.clientEnable) {
      return true;
    }

    var pass = true;
    var validateErrs = {};
    var fieldRules= rules.fields;

    for (var rIndex = 0; rIndex < fieldRules.length; rIndex++) {
      var fieldRule = fieldRules[rIndex];
      var value = formData[fieldRule.name];

      var v = valiField(value,
          fieldRule.type, fieldRule.required,
          fieldRule.minLen, fieldRule.maxLen,
          fieldRule.rangeStart, fieldRule.rangeEnd,
          fieldRule.pattern);

      if (!v) {
        pass = false;
        validateErrs[fieldRule.name] = fieldRule.errMsg;
      } else {
        if (validateErrs[fieldRule.name])
          delete validateErrs[fieldRule.name];
      }
    }

    if (pass) {
      return true;
    } else {
      return validateErrs;
    }
  };

  module.exports = vali;

});