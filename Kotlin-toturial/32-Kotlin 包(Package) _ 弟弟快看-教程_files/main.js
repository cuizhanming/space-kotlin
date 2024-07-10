$(function() {
	
	$("#toc ul li a[href*='vip']").css("color", "red");
	
	$("#toc ul li a[href*='vip']").append("<span style='color:#24BE58;'>：VIP 可看</span>");
	
    
	$(".article-content ul li a[href*='vip']").css("color", "#2c3e50");
	$(".article-content ul li a[href*='vip']").before("<span style='color:red;'>VIP 可看：</span>");

	
    $('.zhuanlan-sidebar-wrapper').on("click", function() {
        var width = $(window).width();
        if ($('.zhuanlan-sidebar-wrapper .arrow').attr('class') === "arrow left") {
            $('.zhuanlan-sidebar-wrapper').removeClass('left');
            $('.zhuanlan-sidebar-wrapper .arrow').removeClass('left');
            $('.bd-aside').removeClass('aside-left');
            $('.bd-aside').addClass("aside-right");
            $('.zhuanlan-sidebar-wrapper').addClass("right");
            $('.zhuanlan-sidebar-wrapper .arrow').addClass("right");
            if (width <= 1024) {
                $('.zhuanlan-theme-container .main-content').css("padding-left", "0");
            }
            if (width > 1024 && width <= 1359) {
                $("#tocml").css("max-width", "14rem");
                $('.zhuanlan-theme-container .main-content').css("padding-left", "0.1rem");
            }
            if (width <= 1273) {
                $('.zhuanlan-theme-container .main-content').css("padding-left", "0.1rem");
            }
            if (width > 1359 && width <= 1433) {
                $("#tocml").css("max-width", "14rem");
                $('.zhuanlan-theme-container .main-content').css("padding-left", "0.1rem");
            }
            if (width > 1433) {
                $("#tocml").css("max-width", "17rem");
                $('.zhuanlan-theme-container .main-content').css("padding-left", "");
            }
        } else {
            $('.zhuanlan-sidebar-wrapper').removeClass('right');
            $('.zhuanlan-sidebar-wrapper .arrow').removeClass('right');
            $('.bd-aside').removeClass('aside-right');
            $('.bd-aside').addClass("aside-left");
            $('.zhuanlan-sidebar-wrapper').addClass("left");
            $('.zhuanlan-sidebar-wrapper .arrow').addClass("left");
            if (width <= 1024) {
                $('.zhuanlan-theme-container .main-content').css("padding-left", "20rem");
            }
            if (width > 1024 && width <= 1359) {
                $('.zhuanlan-theme-container .main-content').css("padding-left", "20rem");
            }
            if (width >= 1359) {
                $("#tocml").css("max-width", "13rem");
            }
            if (width > 1359 && width <= 1433) {
                $("#tocml").css("max-width", "13rem");
                $('.zhuanlan-theme-container .main-content').css("padding-left", "20rem");
            }
            if (width > 1433) {
                $('.zhuanlan-theme-container .main-content').css("padding-left", "");
            }
        }
    });
    $('.index-sidebar-wrapper').on("click", function() {
        var width = $(window).width();
        if ($('.index-sidebar-wrapper .arrow').attr('class') === "arrow left") {
            if (width <= 991) {
                $('.theme-container .main-content').css("padding-left", "2.5rem");
                var left = $(".index-sidebar-wrapper").css("left");
                if (left === "35px") {
                    $('.theme-container .main-content').css("padding-left", "13.5rem");
                    $('.theme-container .bd-aside').css("transform", "translate(0)");
                    $('.index-sidebar-wrapper').css("transform", "translate(0)");
                    $('.index-sidebar-wrapper').css("left", "175px");
                    $('.index-sidebar-wrapper .arrow').css("transform", "rotate(-90deg)");
                    return;
                }
                if (left === "175px") {
                    $('.index-sidebar-wrapper .arrow').removeClass('left');
                    $('.index-sidebar-wrapper .arrow').addClass("right");
                    $('.theme-container .bd-aside').addClass("aside-right");
                    $('.theme-container .bd-aside').removeClass('aside-left');
                    $('.theme-container .bd-aside').css("transform", "translate(-100%)");
                    $('.index-sidebar-wrapper .arrow').css("transform", "rotate(90deg)");
                    $(".index-sidebar-wrapper").css("left", "0px");
                    $('.theme-container .main-content').css("padding-left", "1rem");
                    return;
                }
            }
            $('.index-sidebar-wrapper .arrow').removeClass('left');
            $('.index-sidebar-wrapper .arrow').addClass("right");
            $('.theme-container .bd-aside').css("transform", "translate(-100%)");
            $('.theme-container .bd-aside').removeClass('aside-left');
            $('.theme-container .bd-aside').addClass("aside-right");
            $(".theme-container .index-sidebar-wrapper").css("left", "0px");
            if (width > 991 && width <= 1024) {
                $('.theme-container .main-content').css("padding-left", "1rem");
            }
            if (width > 1024 && width <= 1433) {
                $('.theme-container .main-content').css("padding-left", "2.5rem");
            }
        } else {
            var left = $(".index-sidebar-wrapper").css("left");
            $('.bd-aside').addClass("aside-left");
            $('.theme-container .bd-aside').removeClass('aside-right');
            $('.theme-container .bd-aside').css("transform", "translate(0)");
            $('.index-sidebar-wrapper .arrow').removeClass('right');
            $(".index-sidebar-wrapper").css("left", "175px");
            $('.index-sidebar-wrapper .arrow').addClass("left");
            $('.index-sidebar-wrapper .arrow').css("transform", "rotate(-90deg)");
            if (width <= 991) {
                $('.theme-container .main-content').css("padding-left", "13.5rem");
            }
            if (width > 991 && width <= 1024) {
                $('.theme-container .main-content').css("padding-left", "13.5rem");
            }
            if (width > 1024 && width <= 1433) {
                $('.theme-container .main-content').css("padding-left", "13rem");
            }
        }
    });
    $('.sidebar-heading').on("click", function() {
        if ($(this).next().css("max-height") === "3000px") {
            $(this).next().removeClass(" sidebar-filter-show")
            $(this).next().addClass(" sidebar-filter-hide")
            $(this).children().last().removeClass("down")
            $(this).children().last().addClass("right")
        } else {
            $(this).next().removeClass(" sidebar-filter-hide")
            $(this).next().addClass(" sidebar-filter-show")
            $(this).children().last().removeClass("right")
            $(this).children().last().addClass("down")
        }
    });
    generateCatalog();
    $(window).scroll(function() {
        var _catalog = $(".catalog-h");
        var sctop = $(this).scrollTop();
        _catalog.each(function() {
            var park = $(this).offset().top - sctop;
            var height = $(this).height();
            var _name = $(this).attr("name");
            if (park > 0 && park < 135) {
                $(".article-catalog a").removeClass("active");
                $(".article-catalog a").parent().removeClass("active");
                $(".article-catalog-content a[name='" + _name + "']").addClass("active");
                $(".article-catalog-content a[name='" + _name + "']").parent().addClass("active");
            }
        })
    })
    $(".article-catalog a").on("click", function() {
        $(".article-catalog a").removeClass("active");
        $(this).addClass("active");
        var _name = $(this).attr("name");
        var _ar_top = $(".article-content ." + _name).offset().top;
        $("html,body").animate({
            scrollTop: _ar_top - 70
        }, 10);
    })

    function generateCatalog() {
        var jquery_h1_list = $(".article-content h1");
        var _catalog = "2";
        if (jquery_h1_list.length != 0) {
            generateCatalog_h("h1", "h2", "h3");
            _catalog = "1";
        } else {
            var jquery_h2_list = $(".article-content h2");
            if (jquery_h2_list.length != 0) {
                generateCatalog_h("h2", "h3", "h4");
                _catalog = "1";
            } else {
                var jquery_h3_list = $(".article-content h3");
                if (jquery_h3_list.length != 0) {
                    generateCatalog_h("h3", "h4", "h5");
                    _catalog = "1";
                }
            }
        }
    }
    //HTML转义
    function HTMLEncode(html) {
        var temp = document.createElement("div");
        (temp.textContent != null) ? (temp.textContent = html) : (temp.innerText = html);
        var output = temp.innerHTML;
        temp = null;
        return output;
    }

    function generateCatalog_h(_h1, _h2, _h3) {
        var content = '';
        // 一级目录 start
        content += '<ul class="toc-list first_class_ul">';
        var jquery_h1_list = $(".article-content " + _h1);
        for (var i = 0; i < jquery_h1_list.length; i++) {
            var go_to_top = '<div style="text-align: right"><a name="_label' + i + '"></a></div>';
            $(jquery_h1_list[i]).before(go_to_top);
            $(jquery_h1_list[i]).addClass("label" + i);
            $(jquery_h1_list[i]).addClass("catalog-h");
            $(jquery_h1_list[i]).attr("name", "label" + i);
            // 一级目录的一条
            var li_content = '<li  class="toc-item"  ><a  href="javascript:;;" name="label' + i + '" rel="external nofollow">' + HTMLEncode($(jquery_h1_list[i]).text()) + '</a></li>';
            var nextH1Index = i + 1;
            if (nextH1Index == jquery_h1_list.length) {
                nextH1Index = 0;
            }
            var jquery_h2_list = $(jquery_h1_list[i]).nextUntil(jquery_h1_list[nextH1Index], _h2);
            // 二级目录 start
            if (jquery_h2_list.length > 0) {
                //li_content +='<ul style="list-style-type:none; text-align: left; margin:2px 2px;">';
                li_content += '<ul class="toc-list second_class_ul">';
            }
            for (var j = 0; j < jquery_h2_list.length; j++) {
                var go_to_top2 = '<div style="text-align: right"><a name="lab2_' + i + '_' + j + '"></a></div>';
                $(jquery_h2_list[j]).before(go_to_top2);
                $(jquery_h2_list[j]).addClass("lab2_" + i + '_' + j);
                $(jquery_h2_list[j]).addClass("catalog-h");
                $(jquery_h2_list[j]).attr("name", "lab2_" + i + '_' + j);
                // 二级目录的一条
                li_content += '<li  class="toc-item"  ><a   href="javascript:;;" name="lab2_' + i + '_' + j + '" rel="external nofollow" >' + HTMLEncode($(jquery_h2_list[j]).text()) + '</a></li>';
                var nextH2Index = j + 1;
                var next;
                if (nextH2Index == jquery_h2_list.length) {
                    if (i + 1 == jquery_h1_list.length) {
                        next = jquery_h1_list[0];
                    } else {
                        next = jquery_h1_list[i + 1];
                    }
                } else {
                    next = jquery_h2_list[nextH2Index];
                }
                var jquery_h3_list = $(jquery_h2_list[j]).nextUntil(next, _h3);
                // 三级目录 start
                if (jquery_h3_list.length > 0) {
                    li_content += '<ul class="toc-list third_class_ul">';
                }
                for (var k = 0; k < jquery_h3_list.length; k++) {
                    var go_to_third_Content = '<div style="text-align: right"><a name="_label3_' + i + '_' + j + '_' + k + '"></a></div>';
                    $(jquery_h3_list[k]).before(go_to_third_Content);
                    $(jquery_h3_list[k]).addClass("lab3_" + i + '_' + j + '_' + k);
                    $(jquery_h3_list[k]).addClass("catalog-h");
                    $(jquery_h3_list[k]).attr("name", "lab3_" + i + '_' + j + '_' + k);
                    // 三级目录的一条
                    li_content += '<li   class="toc-item"  ><a  href="javascript:;;" name="lab3_' + i + '_' + j + '_' + k + '" rel="external nofollow" >' + HTMLEncode($(jquery_h3_list[k]).text()) + '</a></li>';
                }
                if (jquery_h3_list.length > 0) {
                    li_content += '</ul>';
                }
                li_content += '</li>';
                // 三级目录 end
            }
            if (jquery_h2_list.length > 0) {
                li_content += '</ul>';
            }
            li_content += '</li>';
            // 二级目录 end
            content += li_content;
        }
        // 一级目录 end
        content += '</ul>';
        $('#article-catalog').prepend(content);
    }
    $(".index-sidebar").click(function() {
        //注释：给当前点击选中的li元素添加selected样式，给同胞元素删除selected样式
        $(this).addClass("index-sidebar-active").siblings().removeClass("index-sidebar-active");
    });
});