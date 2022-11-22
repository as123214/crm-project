<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--	bootstrap--%>
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bs_pagination-master\css\jquery.bs_pagination.min.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

	<script type="text/javascript">
	//整个页面加载完毕，调用此函数
	$(function(){
		//给保存按钮添加单击事件
		$("#saveCreateActivity").click(function(){
			//初始化工作
			//重置表单
			// $("#createActivityForm,.form-control").get(0).reset();未明确原因:导致姓名文本框获取空值
			//获取参数
			let owner=$("#create-marketActivityOwner").val()
			let nameA=$.trim($("#create-marketActivityName").val())
			let startDate=$("#create-startTime").val()
			let endDate=$("#create-endTime").val()
			let cost=$.trim($("#create-cost").val())
			let description=$.trim($("#create-describe").val())
			console.log($("#create-marketActivityName").val())
			console.log('trim:'+$.trim($("#create-marketActivityName").val()))
			console.log(nameA)
			if(owner==''){
				alert("所有者不能为空");
				return;
			}
			if(nameA==''){
				alert("姓名不能为空")
				return;
			}
			if(startDate!=''&&endDate!=''){
				if(endDate<=startDate){

					alert("结束时间不能小于起始时间")
					return;
				}

			}
			var regExp=/^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能为非负整数")
				return;
			}
			//发送请求
			$.ajax({
				url:"workbench/activity/saveCreateActivity.do",
				data:{
					owner:owner,
					name:nameA,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				dataType:"json",
				success:function(data){
					if(data.code==1){
						//关闭模态窗口
						$("#createActivityModal").modal("hide")
						queryActivityByCondition(1,10)
					}else {
						//提示信息
						alert(data.message)
						//显示模态窗口
						$("#createActivityModal").modal("show")

					}

				}

			})
		})
		//当容器加载完毕，对容器调用工具函数
		$("#create-startTime").datetimepicker({
			language:"zh-CN",//语言
			format:"yyyy-mm-dd",//日期格式
			minView:"month",//最小显示单位
			autoclose:true,//选择后是否自动关闭
			initialDate:new Date(),//初始选择日期
			todayBtn:true,//使用‘今天’按钮
			clearBtn:true//使用'清空'按钮
		})
		$("#create-endTime").datetimepicker({
			language:"zh-CN",
			format:"yyyy-mm-dd",
			minView:"month",
			autoclose:true,
			initialDate:new Date(),
			todayBtn:true,
			clearBtn:true
		})
		//查询市场活动第一页,活动总条数
		queryActivityByCondition(1,10)
		//查询所有符合查询条件的，市场活动第一页
		$("#queryActivityByCondition").click(function(){
			queryActivityByCondition(1,$("#my_pagination").bs_pagination('getOption','rowsPerPage'))
		})

		//给全选按钮添加单击事件
		$("#checkAll").click(function(){
			//根据全选按钮状态改变全体市场活动状态
			console.log(this.checked)
			$("#ActivityT input[type='checkbox']").prop("checked",this.checked);
		})

		//给市场活动复选框添加事件
		$("#ActivityT").on("click","input[type='checkbox']",function () {
			//根据所有市场活动复选框选中个数，改变全选框状态
			var allc=$("#ActivityT input[type='checkbox']").size()
			var allcd=$("#ActivityT input[type='checkbox']:checked").size()

			if(allc==allcd){

				$("#checkAll").prop("checked",true);
			}else {

				$("#checkAll").prop("checked",false);
			}

		})

		//给删除按钮添加事件
		$("#deleteByIds").click(function(){
			//获取参数值
			var inputlist=$("#ActivityT input[type='checkbox']:checked")
			console.log("市场活动选中个数"+inputlist.length)
			//判断是否选择市场活动
			if(inputlist.length==0){
				alert("请选择要删除的市场活动")
				return;
			}
			//拼接参数
			var ids=''
			$.each(inputlist,function(){
				console.log(this.value)
				ids+='id='+this.value+"&"
			})
			//截取字符串
			ids=ids.substr(0,ids.length-1)
			console.log(ids)
			var conf=window.confirm("确定删除吗？")
			if(conf==true){

				$.ajax({
					url:"workbench/activity/deleteActivity.do",
					data:ids,
					type:"POST",
					dataType:"json",
					success:function(data){
						if(data.code=="1"){
							queryActivityByCondition(1,$("#my_pagination").bs_pagination('getOption','rowsPerPage'))
							console.log("ajax函数执行了if")
						}else {
							alert(data.message)
							console.log("数据返回失败")
						}
					}
				})
			}else {
				console.log("if未执行")
			}
		})
		//给修改按钮添加单击事件
		$("#modifyById").click(function(){
			//判断是否选择市场活动
		var checkedNum=	$("#ActivityT input[type='checkbox']:checked").length
			if(checkedNum==0){
				alert("至少选择一个市场活动")
				return;
			}else if(checkedNum>1){
				alert("只能选择一个市场活动")
				return;
			}
			$("#editActivityModal").modal("show")
			//发送ajax请求查询市场活动信息
			var owner=$("#ActivityT input[type='checkbox']:checked").val()
			$.ajax({
				url:"workbench/activity/queryActivityById.do",
				data:{
					id:owner
				},
				type:"GET",
				dataType:"json",
				success:function(data){
					//对修改窗口信息赋值
					$("#edit-id").val(data.id);
					$("#edit-marketActivityName").val(data.name);
					console.log(new Date(data.startDate))
					console.log(data.endDate)
					$("#edit-startTime").datetimepicker({
						language:"zh-CN",
						format:"yyyy-mm-dd",
						minView:"month",
						autoclose:true,
						initialDate:new Date(data.startDate),
						todayBtn:true,
						clearBtn:true
					})
					$("#edit-startTime").val(data.startDate)
					$("#edit-endTime").datetimepicker({
						language:"zh-CN",
						format:"yyyy-mm-dd",
						minView:"month",
						autoclose:true,
						initialDate:new Date(data.endDate),
						todayBtn:true,
						clearBtn:true
					})
					$("#edit-endTime").val(data.endDate)
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description);

				}

			})
		})
		//给更新按钮添加事件，更新数据
        $("#updateActivity").click(function(){

            //收集参数
           var id= $("#edit-id").val();
           var owner=$("#edit-marketActivityOwner").val()
          var name=$.trim($("#edit-marketActivityName").val());
          var startDate=$("#edit-startTime").val()
           var endDate=$("#edit-endTime").val()
           var cost=$("#edit-cost").val();
           var description=$("#edit-describe").val();
			console.log($("#edit-marketActivityName").val())
			console.log(name)
           //条件判断
            if(owner==''){
                alert("所有者不能为空");
                return;
            }
            if(name==''){
                alert("姓名不能为空")
                return;
            }
            if(startDate!=''&&endDate!=''){
                if(endDate<=startDate){

                    alert("结束时间不能小于起始时间")
                    return;
                }

            }
            var regExp=/^(([1-9]\d*)|0)$/;
            if(!regExp.test(cost)){
                alert("成本只能为非负整数")
                return;
            }
			console.log(name)

			//发送请求
            $.ajax({
                url:"workbench/activity/saveEditActivity.do",
                data:{
                    id:id,
                    owner:owner,
					name:name,
                    startDate:startDate,
                    endDate:endDate,
                    cost:cost,
                    description:description
                },
                type:"POST",
                dataType:"json",
                success:function(data){
                    if(data.code=='1'){
                        queryActivityByCondition($("#my_pagination").bs_pagination('getOption','currentPage'),$("#my_pagination").bs_pagination('getOption','rowsPerPage'))
						$("#editActivityModal").modal("hide")
						console.log("数据更新成功")
                    }else {
                        alert("跟新失败")
                        $("#editActivityModal").modal("show")
                    }

                }
            })
        })
		//给导出市场活动添加单击事件
		$("#exportActivityAllBtn").click(function(){
			window.location.href="workbench/activity/faileDownload.do"
		})
        //给选择导出市场活动添加单击事件
        // /workbench/activity/queryActivityByIds.do
        $("#exportActivityXzBtn").click(function(){
            var cds=$("#ActivityT input[type='checkbox']:checked")
            var cd=$("#ActivityT input[type='checkbox']:checked").val()
			if(cds.length==0){
				alert("至少选择一个")
				return;
			}
			var ids=''
			$.each(cds,function(){
				console.log(this.value)
				ids+='id='+this.value+"&"
			})
            ids=ids.substring(0,ids.length-1)
			console.log(ids)
			document.location.href="workbench/activity/queryActivityByIds.do?"+ids

        })
		//给‘导入’按钮添加事件
		$("#importActivityBtn").click(function(){
			//对文件类型及其大小进行条件判断
			var file=$("#activityFile")[0].files[0]
			var fileName=$("#activityFile").val()
			var fileType=fileName.substr(fileName.lastIndexOf(".")+1).toLocaleUpperCase()
			if(fileType!='XLS')
			{
				alert("上传文件只支持.xls文件")
				return;
			}
			if(fileType.size>5*1024*1024){
				alert("文件大小不能大于5M")
				return;
			}
			var formdata=new FormData();
			formdata.append("myfile",file)
			console.log(file)
			$.ajax({
				url:"workbench/activity/saveActivityByFile.do",
				dataType:"json",
				type:"POST",
				processData:false,//设置ajax向后台提交参数之前,是否将参数统一转换成字符串，默认是true
				contentType:false,//设置ajax向后台提交参数之前,是否将所有的参数统一按urlencoded编码，默认是true
				data:formdata,
				success:function(data){
					if(data.code=='1'){
						//关闭模态窗口
						$("#importActivityModal").modal("hide")
						queryActivityByCondition(1,$("#my_pagination").bs_pagination('getOption','rowsPerPage'))

					}else {
						$("#importActivityModal").modal("show")
						alert(data.message)
					}
				}
			})
		})


	});
	//封装查询活动函数
function queryActivityByCondition(pageNo,pageSize){
		//显示市场活动第一页内容，和所有活动的个数
		//获取查询参数
		var name=$("#query-name").val()
		var owner=$("#query-owner").val()
		var startDate=$("#query-startTime").val()
		var endDate=$("#query-endTime").val()


		$.ajax({
			url:"workbench/activity/queryActivity.do",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				pageNo:pageNo,
				pageSize:pageSize,
				endDate:endDate
			},
			type:"POST",
			dataType:"json",
			success:function(data){
				//显示总条数
				$("#totalRows").text(data.totalRows)
				//拼接字符串，显示市场活动
				var html=''
				$.each(data.activityList,function(index,obj){
					html+="<tr class=\"active\">"
					html+="	<td><input type=\"checkbox\" value="+obj.id+"></td>"
					html+="	<td><a  style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/queryActivityForDetail.do?id="+obj.id+"'\">"+obj.name+"</a></td>"
					html+="	<td>"+obj.owner+"</td>"
					html+="	<td>"+obj.startDate+"</td>"
					html+="	<td>"+obj.endDate+"</td>"
					html+="</tr>"
				})
				//重置全选按钮
				$("#checkAll").prop("checked",false);
				//计算总页数
				var totalPage=1
				if(data.totalRows%pageSize==0){
					totalPage=data.totalRows/pageSize
				}else {
					totalPage=parseInt(data.totalRows/pageSize)+1
				}
				$("#ActivityT").html(html)
				$("#my_pagination").bs_pagination({
					currentPage:pageNo,//当前页数

					rowsPerPage:pageSize,//每页条数
					totalRows:data.totalRows,//总体条数
					totalPages:totalPage,//总页数

					visiblePageLinks: 5,//最多可以显示的卡片数

					showGoToPage: true,//是否显示跳转到部分，默认为true
					showRowsPerPage: true,//是否显示每页显示条数部分,默认为true
					showRowsInfo: true,//是否显示记录的信息,默认为true

					onChangePage:function(event,pageObject){
						console.log("当前页码"+pageObject.currentPage)
						queryActivityByCondition(pageObject.currentPage,pageObject.rowsPerPage)
					}


						}
				)
			}
		})
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${users}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-startTime" >
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-endTime" >
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost" >
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveCreateActivity">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${users}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivity">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityByCondition">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivitybtn" data-toggle="modal" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="modifyById" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteByIds"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="ActivityT">
					<%--						<tr class="active">
                                                <td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
				<div id="my_pagination"></div>
			</div>
			


			</div>
			
		</div>
		
	</div>
</body>
</html>