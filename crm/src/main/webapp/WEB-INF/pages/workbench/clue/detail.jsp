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

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function(){
            $("#remark").focus(function(){
                if(cancelAndSaveBtnDefault){
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height","130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function(){
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height","90px");
                cancelAndSaveBtnDefault = true;
            });

           /* $(".remarkDiv").mouseover(function(){
                $(this).children("div").children("div").show();
            });*/
            $("#remarkList").on("mouseover",".remarkDiv",function () {
                $(this).children("div").children("div").show();

            })
         /*   $(".remarkDiv").mouseout(function(){
                $(this).children("div").children("div").hide();
            });*/
            $("#remarkList").on("mouseout",".remarkDiv",function () {
                $(this).children("div").children("div").hide();

            })
          /*  $(".myHref").mouseover(function(){
                $(this).children("span").css("color","red");
            });*/
            $("#remarkList").on("mouseover",".myHref",function () {
                $(this).children("span").css("color","red");
            })
        /*    $(".myHref").mouseout(function(){
                $(this).children("span").css("color","#E6E6E6");
            });*/
            $("#remarkList").on("mouseout",".myHref",function () {
                $(this).children("span").css("color","#E6E6E6");
            })
            //给’保存‘按钮添加单击事件
            $("#saveClueRemark").click(function(){
                //收集参数
                var createBy="${clue.fullname}${clue.appellation}";
                var noteContent=$("#remark").val()
                var clueId="${clue.id}"
                console.log(noteContent)
                //发送ajax请求
                $.ajax({
                    url:"workbench/clue/saveClueRemark.do",
                    data:{
                        createBy:createBy,
                        noteContent:noteContent,
                        clueId:clueId
                    },
                    type:"POST",
                    dataType:"json",
                    success:function(data){
                        //根据返回结果，进行条件判断
                        if(data.code=='1'){
                            //清空评论框
                            $("#remark").val('')
                            //追加评论
                            var strhtml=''
                            strhtml+="<div id=\"div_"+data.retData.id+"\" class=\"remarkDiv\" style=\"height: 60px;\">"
                            strhtml+="<img title=\"${sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">"
                            strhtml+="<div style=\"position: relative; top: -40px; left: 40px;\" >"
                            strhtml+="<h5 id=\"h5_"+data.retData.id+"\">"+data.retData.noteContent+"</h5>"
                            strhtml+="<font color=\"gray\">线索</font> <font color=\"gray\">-</font> <b>${clue.fullname}${clue.appellation}-${clue.company}</b> <small style=\"color: gray;\">"+data.retData.createTime+"由${clue.fullname}创建</small>"
                            strhtml+="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">"
                            strhtml+="<a class=\"myHref\" name=\"editA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"
                            strhtml+="&nbsp;&nbsp;&nbsp;&nbsp;"
                            strhtml+="<a class=\"myHref\" name=\"deleteA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>"
                            strhtml+="</div>"
                            strhtml+="</div>"
                            strhtml+="</div>"

                            $("#remarkDiv").before(strhtml)
                        }
                    }
                })
            })
            //给’X‘按钮添加事件
            $("#remarkList").on("click","a[name='deleteA']",function(){
                //收集参数
                var id=$(this).attr("remarkId")
                $.ajax({
                    url:"workbench/clue/deleteClurRemark.do",
                    data:{
                        id:id
                    },
                    type:"POST",
                    dataType:"json",
                    success:function(data){
                        if(data.code=='1'){
                            $("#div_"+id).remove()
                        }else {
                            alert(data.message)
                        }
                    }
                })
            })
            //给‘关联市场活动‘按钮添加事件
            $("#bondActivitybtn").click(function(){
                //准备工作
                //收集参数
                var activityName=$(this).val()
                var clueId='${clue.id}'
                $.ajax({
                    url:"workbench/clue/queryActivityForDetail.do",
                    data:{
                        activityName:activityName,
                        clueId:clueId
                    },
                    type:"post",
                    dataType:"json",
                    success:function(data){
                        console.log(data)
                        var htm=''
                        $.each(data,function(index,obj){
                            htm+="<tr>";
                            htm+=" <td><input type=\"checkbox\" value= \""+obj.id+"\"/></td>";
                            htm+="<td>"+obj.name+"</td>";
                            htm+="<td>"+obj.startDate+"</td>";
                            htm+=" <td>"+obj.endDate+"</td>";
                            htm+="<td>"+obj.owner+"</td>";
                            htm+="</tr>";
                        })
                        $("#activityList").html(htm)
                    }

                })
                    //显示绑定市场活动模态窗口
                $("#bundModal").modal("show")
            })
            //给搜索框添加键盘弹起事件
            $("#serachA").keyup(function(){
                //收集参数
                var activityName=$(this).val()
                var clueId='${clue.id}'
                $.ajax({
                    url:"workbench/clue/queryActivityForDetail.do",
                    data:{
                        activityName:activityName,
                        clueId:clueId
                    },
                    type:"post",
                    dataType:"json",
                    success:function(data){
                        console.log(data)
                        var htm=''
                        $.each(data,function(index,obj){
                            htm+="<tr>";
                            htm+=" <td><input type=\"checkbox\" value= \""+obj.id+"\"/></td>";
                            htm+="<td>"+obj.name+"</td>";
                            htm+="<td>"+obj.startDate+"</td>";
                            htm+=" <td>"+obj.endDate+"</td>";
                            htm+="<td>"+obj.owner+"</td>";
                            htm+="</tr>";
                        })
                        $("#activityList").html(htm)
                    }

                })
            })
            //给’关联‘按钮添加单击事件
            $("#bundActivity").click(function(){
                //收集参数
                var ids=$("#activityTable input[type='checkbox']:checked")
                var id=''
                $.each(ids,function(){
                    id+="id="+this.value+"&"
                })
                id+="clueId=${clue.id}"
                console.log(id)
                //发送请求
                $.ajax({
                    url:"workbench/clue/saveClueRelation.do",
                    data:id,
                    type:"post",
                    dataType:"json",
                    success:function(data){
                        if(data.code=='1'){
                            //关闭模态窗口
                            $("#bundModal").modal("hide")
                            console.log(data.retData)
                            console.log(data)
                            //刷新关联市场活动
                            var htmlStr=''
                            $.each(data.retData,function(index,obj){
                                htmlStr+="<tr id=\"tr_"+obj.id+"\">";
                                htmlStr+="<td>"+obj.name+"</td>";
                                htmlStr+="<td"+obj.startDate+"</td>";
                                htmlStr+="<td>"+obj.endDate+"</td>";
                                htmlStr+="<td>"+obj.owner+"</td>";
                                htmlStr+="<td><a href=\"javascript:void(0);\" activityId=\""+obj.id+"\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>解除关联</a></td>";
                                htmlStr+="</tr>";
                                $("#relationTbody").append(htmlStr)
                            })

                        }else{
                            //显示模态窗口
                            $("#bundModal").modal("show")
                            //提示错误信息
                            alert(data.message)
                        }
                    }
                })
            })
            //给‘解除关联'按钮添加单击事件
            $("#relationTbody").on("click","a",function(){
                //收集参数
                var activityId=$(this).attr("activityId")
                var clueId='${clue.id}'
               var flag=confirm("是否删除关联市场活动")
                if(flag){
                    //发送ajax请求
                    $.ajax({
                        url:"workbench/clue/saveUnbund.do",
                        data:{
                            activityId:activityId,
                            clueId:clueId
                        },
                        type:"POST",
                        dataType:"json",
                        success:function(data){
                            if(data.code=='1'){
                                //删除对应市场活动
                                $("#tr_"+activityId).remove()
                            }else {
                                alert(data.message)
                            }
                        }
                    })
                }

            })
            //给"转换"按钮添加单击事件
            $("#convertBtn").click(function(){
                window.location.href="workbench/clue/toConvert.do?id=${clue.id}"
            })
        });

    </script>

</head>
<body>

<!-- 关联市场活动的模态窗口 -->
<div class="modal fade" id="bundModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">关联市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" id="serachA" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox"/></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="activityList">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="bundActivity" data-dismiss="modal">关联</button>
            </div>
        </div>
    </div>
</div>


<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>${clue.fullname}<span>${clue.appellation}</span> <small>${clue.company}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" id="convertBtn"><span class="glyphicon glyphicon-retweet"></span> 转换</button>

    </div>
</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.fullname}<span>${clue.appellation}</span></b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">公司</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.company}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.job}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">邮箱</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.email}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.phone}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">公司网站</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.website}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.mphone}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">线索状态</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.state}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.source}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 70px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${clue.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 80px;">
        <div style="width: 300px; color: gray;">联系纪要</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${clue.contactSummary}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 90px;">
        <div style="width: 300px; color: gray;">下次联系时间</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.nextContactTime}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 100px;">
        <div style="width: 300px; color: gray;">详细地址</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${clue.address}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 40px; left: 40px;" id="remarkList">
    <div class="page-header">
        <h4>备注</h4>
    </div>
<c:forEach items="${clueRemarks}" var="cr">
    <div class="remarkDiv" id="div_${cr.id}" style="height: 60px;">
        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;" >
            <h5>${cr.noteContent}</h5>
            <font color="gray">线索</font> <font color="gray">-</font> <b>${clue.fullname}${clue.appellation}-${clue.company}</b> <small style="color: gray;"> ${cr.editFlag=='1'?cr.editTime:cr.createTime} ${cr.editFlag=='1'?cr.editBy:cr.createBy}${cr.editFlag=='1'?'修改':'创建'} </small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);" name="editA" remarkId="${cr.id}"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);" name="deleteA" remarkId="${cr.id}"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>
</c:forEach>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" id="saveClueRemark">保存</button>
            </p>
        </form>
    </div>
</div>

<!-- 市场活动 -->
<div>
    <div style="position: relative; top: 60px; left: 40px;">
        <div class="page-header">
            <h4>市场活动</h4>
        </div>
        <div style="position: relative;top: 0px;">
            <table class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>名称</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                    <td>所有者</td>
                    <td></td>
                </tr>
                </thead>
                <tbody id="relationTbody">
                <c:forEach items="${activityList}" var="a">
                    <tr id="tr_${a.id}">
                        <td>${a.name}</td>
                        <td>${a.startDate}</td>
                        <td>${a.endDate}</td>
                        <td>${a.owner}</td>
                        <td><a href="javascript:void(0);" activityId="${a.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>

        <div>
            <a href="javascript:void(0);" id="bondActivitybtn" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
        </div>
    </div>
</div>


<div style="height: 200px;"></div>
</body>
</html>