<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<style type="text/css">
.tab-content { padding: 1em;}
p { text-indent: 2em;}
</style>

<div class="container">
<div class="row">
	<ul class="nav nav-tabs" id="announcementTab">
	  <li class="active"><a href="#app">App使用</a></li>
	  <li><a href="#lotteries">现场抽奖</a></li>
	  <li><a href="#votes">节目投票</a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active" id="app">
			<div>
				<dl>
					<dt>用户名</dt>
					<dd>每位同学的用户名为学号，老师及嘉宾的用户名为姓名拼音全拼</dd>
				</dl>
				<dl>
					<dt>密码</dt>
					<dd>每位同学的密码为身份证后6位，不能登录的请使用“123456”登录；老师及嘉宾为指定密码</dd>
				</dl>
				<dl>
					<dt>昵称</dt>
					<dd>默认为姓名，可自由修改</dd>
				</dl>
				<dl>
					<dt>吐槽内容</dt>
					<dd>字数介于5~140个字符。请各位同学文明吐槽，发现不良信息将追究到人</dd>
				</dl>
				<dl>
					<dt>点赞/点踩</dt>
					<dd>每位同学可以对任意一条评论最多点一次赞/踩</dd>
				</dl>
			</div>
		</div>
		
		<div class="tab-pane" id="lotteries">
			<div>
				<h4>奖项设置</h4>
				<ul>
					<li>特等奖  1名： 最新Kindle paperwhite II</li>
					<li>一等奖  2名： 公文包</li>
					<li>二等奖  10名：钱包*7+皮带*3 （随机发放）</li>
					<li>三等奖  10名：零食大礼包</li>
					<li>幸运奖  20名：暖手宝 </li>
					<li>特设：点赞狂人奖/点踩狂人奖/不明觉厉评论奖/累觉不爱评论奖各一名，奖品为零食大礼包一份</li>
				</ul>
			</div>
			<div>
				<h4>抽奖规则</h4>
				<ul>
					<li>取每位同学所发布评论中被点赞次数最多的进入抽奖池</li>
					<li>抽奖池中每个条评论的权值公式：<p class="text-center"><img src="resources/images/lottery-rule1.jpg" /><p>权值越大，抽中的概率越大</li>
					<li>已中奖者的评论不能再进入抽奖池</li>
					<li>点赞狂人奖、点踩狂人奖、不明觉厉评论奖、累觉不爱评论奖不占抽奖名额</li>
					<li>循环滚动抽奖池中的评论，被选中的评论的发布者即为中奖者</li>
				</ul>
			</div>
			
			<div>
				<h4>特别鸣谢：</h4>
			<div class="panel-group" id="sponsorsAccordion">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" data-parent="#sponsorsAccordion" href="#zhonghang">中国银行宁波市科技支行</a>
						</h4>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" data-parent="#sponsorsAccordion" href="#jibo">浙江吉博教育科技有限公司</a>
						</h4>
					</div>
					<div id="jibo" class="panel-collapse collapse in">
						<div class="panel-body">
							<p>浙江吉博教育科技有限公司是一家致力于智慧教育体系建设的教育高科技企业，公司将利用云计算、物联网、互联网、公共服务平台和先进的云端设备，为政府、学校、企业、学员提供最佳网络教育解决方案。公司教育科技产品有效依托计算机互联网与移动互联网，让学员随时随地学习。</p>
							<p>吉博教育科技成立于2011年，总部位于中国宁波。在中国的北京、杭州、上海、温州设有运营中心，并在马来西亚吉隆坡成立了第一家海外运营中心。公司的“技术研发中心”位于宁波的浙大软件学院校区内。</p>
							<p>2012年吉博教育科技还与浙大软件学院合作成立了“智慧教育研究院”实现产学研结合。公司目前拥有300多位技术团队与运营管理团队。</p>
							<p>吉博教育科技是教育部、商务部、工信部、人保部四大部委指定“专业教育机构特约合作单位”“网络课程制作单位”，还获得了全国职业核心能力认证培训网络课程支持单位、中国商业教育培训工作站、宁波市电子商务协会常务理事单位、宁波服务外包协会理事单位、宁波市市场营销协会会员单位、软件企业认证等一系列荣誉。</p>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" data-parent="#sponsorsAccordion" href="#nonghang">中国农业银行高新区支行</a>
						</h4>
					</div>
					<div id="nonghang" class="panel-collapse collapse in">
						<div class="panel-body">
							<p>中国农业银行高新区支行位于江南路1512号 向最广大客户提供各种公司银行和零售银行产品和服务，同时开展自营及代客资金业务，业务范围还涵盖投资银行、基金管理、金融租赁、人寿保险等领域。</p>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a data-toggle="collapse" data-parent="#sponsorsAccordion" href="#tucaiguan">衢州开化农家土菜馆</a>
						</h4>
					</div>
					<div id="tucaiguan" class="panel-collapse collapse in">
						<div class="panel-body">
							<p>衢州开化位于浙江省母亲河——钱塘江的源头生态县。本店菜肴均采用精选食材，保质保量，以最实惠的价格，给你最好的享受。特色菜有:自磨豆腐，开化青蛳，乌笋干，何田清水鱼，脆皮冬瓜等。本店每周定时定量从开化本地接受各类野生菜肴，喜欢的朋友可以提前预定。店内可承办聚餐，生日宴会等活动，有意向者可提前预定，欢迎来店咨询。</p>
							<p>订餐及咨询热线：13325885288。</p>
						</div>
					</div>
				</div>
			</div>
			</div>
		</div>
		
		<div class="tab-pane" id="votes">
			<div>每个学生及嘉宾均可自由选择自己感兴趣的节目并为其投下自己珍贵的一票，每个节目开始时即开放该节目的投票通道，该投票通道将一直持续至晚会结束</div>
			<div>
				<h4>投票规则</h4>
				<ul>
					<li>单人的投票上限为5票，且不可对单一节目重复投票</li>
					<li>学生每一票的权重值为1，嘉宾（含老师及赞助商嘉宾）每一票的权重值为5</li>
					<li>最后将不同节目的总得分（总得分=学生投票数量*1+嘉宾投票数量*5）按高到低的顺序排列选出晚会获奖节目名单</li>
				</ul>
			</div>
		</div>
	</div>
</div>
</div>

<script type="text/javascript">
$(function() {
	$('#announcementTab a').click(function (e) {
  		e.preventDefault();
  		$(this).tab('show');
	});
	
	$('.collapse').collapse();
});
</script>
