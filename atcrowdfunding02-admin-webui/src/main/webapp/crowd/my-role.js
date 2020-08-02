/**
 * 
 */
function generatePage(){
	
	var pageInfo = getPageInfoRemote();
	
	fillTableBody(pageInfo);
	
	
}

function getPageInfoRemote(){
	
	var ajaxResult = $.ajax({
		"url":"role/get/page/info.json",
		"type":"post",
		"data":{
			"pageNum":window.pageNum,
			"pageSize":window.pageSize,
			"keyword":window.keyword
		},
		"async":false,
		"dataType":"json",
		"success":function(response){
			var pageInfo = response.data;
			
			fillTableBody(pageInfo);
		},
		"error":function(response){
			
		}
	});
	
	console.log(ajaxResult);
	
	var statusCode = ajaxResult.status;
	
	if(statusCode != 200){
		layer.msg("服务器端程序调用失败!响应状态吗:"+statusCode+"说明信息="+ajaxResult.statusText);
		return null;
	}
	
	var resultEntity = ajaxResult.responseJSON;
	
	var result = resultEntity.result;
	
	if(result == "FAILED"){
		layer.msg(resultEntity.message);
		return null;
	}
	 
	var pageInfo = resultEntity.data;
	
	return pageInfo;

}

function fillTableBody(pageInfo){
	
	$("#rolePageBody").empty();
	
	if(pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length ==0){
		$("#rolePageBody").append("<tr><td colspan='4'>抱歉！没有数据</td></tr>")
		return ;
		
	}
	
	for(var i=0;i<pageInfo.list.length;i++){
		var role = pageInfo.list[i];
		var roleId = role.id;
		var roleName = role.name;
		var numberTd = "<td>"+(i+1)+"</td>";
		var checkboxTd = "<td><input type='checkbox'></td>";
		var roleNameTd = "<td>"+roleName+"</td>";
		
		var checkBtn = "<button type='button' class='btn btn-success btn-xs'><i class=' glyphicon glyphicon-check'></i></button>"
		var pencilBtn = "<button type='button' class='btn btn-primary btn-xs'><i class=' glyphicon glyphicon-pencil'></i></button>"
		var removeBtn = "<button type='button' class='btn btn-danger btn-xs'><i class=' glyphicon glyphicon-remove'></i></button>"
		
		var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>"
		
		var tr = "<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";
		$("#rolePageBody").append(tr);		
	}
	generateNavigator(pageInfo);
}

function generateNavigator(pageInfo){
	var totalRecord = pageInfo.total;
	var properties = {
			"num_edge_entries": 3,								
			"num_display_entries": 5,							
			"callback": paginationCallBack,						
			"items_per_page": pageInfo.pageSize,	
			"current_page": pageInfo.pageNum - 1,	
			"prev_text": "上一页",									
			"next_text": "下一页"	
	}
	
	$("#Pagination").pagination(totalRecord,properties);
}

function paginationCallBack(pageIndex,jQuery){
	
	window.pageNum = pageIndex + 1;
	
	generatePage();
	
	return false;
}
