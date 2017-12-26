define(function () {
    //Top toolbar
    var mainToolbar = {
        view: "toolbar",

        elements:[
            {view: "label", label: "<a href='http://webix.com'><img class='photo' src='assets/imgs/logo.png' /></a>", width: 200},

            { height:46, id: "person_template", css: "header_person", borderless:true, width: 180, data: {id:3,name: "Oliver Parr"},
                template: function(obj){
                    var html = 	"<div style='height:100%;width:100%;' onclick='webix.$$(\"profilePopup\").show(this)'>";
                    html += "<img class='photo' src='assets/imgs/photos/"+obj.id+".png' /><span class='name'>"+obj.name+"</span>";
                    html += "<span class='webix_icon fa-angle-down'></span></div>";
                    return html;
                }
            },
            {},
            {view: "icon", icon: "search",  width: 45, popup: "searchPopup"},
            {view: "icon", icon: "envelope-o", value: 3, width: 45, popup: "mailPopup"},
            {view: "icon", icon: "comments-o", value: 5, width: 45, popup: "messagePopup"}
        ]
    };

    var body = {
        rows:[
            { height: 49, id: "title", css: "title", template: "<div class='header'>#title#</div><div class='details'>( #details# )</div>", data: {text: "",title: ""}},
            {
                view: "scrollview", scroll:"native-y",
                body:{ cols:[{ $subview:true}] }
            }
        ]
    };
    var menu = webix.type(webix.ui.tree, {
        name:"menuTree2",
        height: 40,

        icon:function(obj, common){
            var html = "";
            var open = "";
            for (var i=1; i<=obj.$level; i++){
                if (i==obj.$level && obj.$count){
                    var dir = obj.open?"down":"right";
                    html+="<span class='"+open+" webix_icon fa-angle-"+dir+"'></span>";
                }
            }
            return html;
        },
        folder:function(obj, common){
            if(obj.icon)
                return "<span class='webix_icon icon fa-"+obj.icon+"'></span>";
            return "";
        }
    });

    var layout = {
        rows:[
            mainToolbar,
            {
                cols:[
                    menu,
                    body
                ]
            }
        ]
    };

    alert(222);

    return {
        $ui:layout,
        $menu:"app:menu",
        $oninit:function(view, scope){
            alert(view);
            alert(scope);
            // scope.ui(search.$ui);
            // scope.ui(mail.$ui);
            // scope.ui(message.$ui);
            // scope.ui(profile.$ui);
        }
    };
});