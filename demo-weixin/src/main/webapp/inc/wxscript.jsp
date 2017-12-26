<%--
  Created by IntelliJ IDEA.
  User: <a href="mailto:88052350@qq.com">chengqiang.han</a>
  Date: 2017/5/4
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<script type="text/javascript" src="<c:url value='/static/js/zepto.min.js' />"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/libs/weuijs/1.0.0/weui.min.js"></script>

<script type="text/javascript">

    /**
     * 格式化
     * '<li class="style_{0}" >{1}</li>'.format(name,value)
     */
    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d{1})}/g, function() {
            return args[arguments[1]];
        });
    }

    /**
     * 日期格式化
     */
    Date.prototype.format = function(mask) {
        var d = this;
        var zeroize = function(value, length) {
            if (!length)
                length = 2;
            value = String(value);
            for ( var i = 0, zeros = ''; i < (length - value.length); i++) {
                zeros += '0';
            }
            return zeros + value;
        };

        return mask
            .replace(
                /"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g,
                function($0) {
                    switch ($0) {
                        case 'd':
                            return d.getDate();
                        case 'dd':
                            return zeroize(d.getDate());
                        case 'ddd':
                            return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri',
                                'Sat' ][d.getDay()];
                        case 'dddd':
                            return [ 'Sunday', 'Monday', 'Tuesday',
                                'Wednesday', 'Thursday', 'Friday',
                                'Saturday' ][d.getDay()];
                        case 'M':
                            return d.getMonth() + 1;
                        case 'MM':
                            return zeroize(d.getMonth() + 1);
                        case 'MMM':
                            return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][d
                                .getMonth()];
                        case 'MMMM':
                            return [ 'January', 'February', 'March', 'April',
                                'May', 'June', 'July', 'August',
                                'September', 'October', 'November',
                                'December' ][d.getMonth()];
                        case 'yy':
                            return String(d.getFullYear()).substr(2);
                        case 'yyyy':
                            return d.getFullYear();
                        case 'h':
                            return d.getHours() % 12 || 12;
                        case 'hh':
                            return zeroize(d.getHours() % 12 || 12);
                        case 'H':
                            return d.getHours();
                        case 'HH':
                            return zeroize(d.getHours());
                        case 'm':
                            return d.getMinutes();
                        case 'mm':
                            return zeroize(d.getMinutes());
                        case 's':
                            return d.getSeconds();
                        case 'ss':
                            return zeroize(d.getSeconds());
                        case 'l':
                            return zeroize(d.getMilliseconds(), 3);
                        case 'L':
                            var m = d.getMilliseconds();
                            if (m > 99)
                                m = Math.round(m / 10);
                            return zeroize(m);
                        case 'tt':
                            return d.getHours() < 12 ? 'am' : 'pm';
                        case 'TT':
                            return d.getHours() < 12 ? 'AM' : 'PM';
                        case 'Z':
                            return d.toUTCString().match(/[A-Z]+$/);
                        default:
                            return $0.substr(1, $0.length - 2);

                    }
                });

    };

    function ajaxSilent(options) {
        var _options = {
            type: 'POST',
            url: '',
            data: {},
            contentType:"application/x-www-form-urlencoded",
            complete:function (xhr, status) {
            },
            error:function (xhr, errorType, error) {
                weui.toast((xhr.responseText || '处理失败'), 2000);
            },
            timeout:0,
            async:true,
            dataType: "json"
        };
        $.ajax($.extend(_options, options));
    }

    function ajaxSimple(options) {
        var _t = { 't_loading': '处理中...','t_succ':'处理成功','t_err':'处理失败'};
        $.extend(_t,options);

        var _cb = {
            success: function(data){
                if(data && data.err) {
                    _t.t_err = data.errMsg || data.errCode || _t.t_err;
                    weui.toast(_t.t_err, 700);
                } else {
                    if (options && options.success) {
                        options.success(data);
                    }
                    weui.toast(_t.t_succ, 700);
                }
            }
        }
        var loading = weui.loading(_t.t_loading);
        var _options = {
            type: 'POST',
            url: '',
            data: {},
            contentType:"application/x-www-form-urlencoded",
            complete:function (xhr, status) {
                loading.hide();
            },
            error:function (xhr, errorType, error) {
                weui.toast((xhr.responseText || _t.t_err), 2000);
            },

            timeout:0,
            async:true,

            dataType: "json"
        };

        $.ajax($.extend(_options, options,_cb));
    }


   function ajaxLoading(options) {
        var _t = { 't_loading': '处理中...','t_succ':'处理成功','t_err':'处理失败'};
        $.extend(_t,options);

        var _cb = {
            success: function(data){
                if(data && data.err) {
                    _t.t_err = data.errMsg || data.errCode || _t.t_err;
                    weui.toast(_t.t_err, 700);
                } else {
                    if (options && options.success) {
                        options.success(data);
                    }
                }
            }
        }
        var loading = weui.loading(_t.t_loading);
        var _options = {
            type: 'POST',
            url: '',
            data: {},
            contentType:"application/x-www-form-urlencoded",
            complete:function (xhr, status) {
                loading.hide();
            },
            error:function (xhr, errorType, error) {
                weui.toast((xhr.responseText || _t.t_err), 2000);
            },

            timeout:0,
            async:true,

            dataType: "json"
        };

        $.ajax($.extend(_options, options,_cb));
    }

    function showMoeny(fen) {
        if (fen==0) {
            return 0;
        }
        return (fen / 100).toFixed(2);
    }
</script>