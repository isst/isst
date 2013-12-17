Date.prototype.format = function(fmt)   {
  var o = {   
    "M+" : this.getMonth()+1, 
    "d+" : this.getDate(),
    "h+" : this.getHours(),
    "m+" : this.getMinutes(),
    "s+" : this.getSeconds(),
    "q+" : Math.floor((this.getMonth()+3)/3),
    "S"  : this.getMilliseconds()
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
};

function newTemplate(spittle) {
	var dateString = (new Date(spittle.postTime * 1000)).format("yyyy-MM-dd hh:mm:ss");
	var template = '<li data-id="'+spittle.spittleId+'" data-name="'+spittle.userName+'">' +
		'<div class="spittle">' +
			'<div class="hd">' +
				'<h3>'+spittle.nickname+'</h3>' +
				'<p>'+dateString+'</p>' +
			'</div>' +
			'<div class="cnt ">'+spittle.content+'</div>' +
		'</div>' +
	'</li>';
    return $(template);
}

$(function() {
	$("#controls").hover(
		function() {
			var $this = $(this);
			if ($this.hasClass("show")) {
				$(this).removeClass("show");
				$(this).addClass("hide");
			} else {
				$(this).removeClass("hide");
				$(this).addClass("show");
			}
		}
	);
});