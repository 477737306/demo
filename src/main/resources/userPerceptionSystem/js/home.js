   // 获取数据接口
   $(function() {
       $(document).ready(function() {
           var publicPlatProjectCount = ''; // 众测平台-项目数
           var publicPlatTestingCount = ''; // 众测平台-在测项目数
           var publicPlatBugCount = ''; // 众测平台-缺陷数
           var publicPlatNewUserCount = ''; // 众测平台-注册用户数
           var userSimProjectCount = ''; // 用户模拟平台-项目数
           var userSimScriptCount = ''; // 用户模拟平台-脚本数
           var userSimBugCount = ''; // 用户模拟平台-缺陷数
           var userSimExecutionCount = ''; // 用户模拟平台-执行次数
           var params = {"ROOT":{"BODY":{"CHECK":"conview"},"HEADER":{"IP":"","TIME":""}}};
           // 获取众测平台数据
           $.ajax({
               url: 'http://192.168.157.94:8080/publicSurveyTask/auth/statistics',
               async: false,
               type: 'post',
               contentType: 'application/json',
               dataType: 'json',
               // xhrFields和crossDomain这两个参数是解决跨域的
               xhrFields: {
                   withCredentials: true
               },
               crossDomain: true,
               data: JSON.stringify(params),
               success: function(data){
                   var resultPlat = data.ROOT.BODY
                   if (resultPlat.CODE === '0000') {
                      publicPlatProjectCount = resultPlat.PRO_NUM; // 项目数
                      publicPlatTestingCount = resultPlat.TESTING_PRO_NUM; // 在测项目数
                      publicPlatBugCount = resultPlat.DEFECT_NUM; // 缺陷数
                      publicPlatNewUserCount = resultPlat.REGISTER_NUM; // 注册用户数
                      $('#projectCounts').before(publicPlatProjectCount);
                      $('#testingCounts').before(publicPlatTestingCount);
                      $('#bugCounts').before(publicPlatBugCount);
                      $('#newUserCounts').before(publicPlatNewUserCount);
                      console.log(publicPlatProjectCount);
                   } else {
                       alert('未获取到数据，请联系后台管理员！')
                   }
               },
               error: function(){
               }
           });

           // 获取用户模拟平台数据
           $.ajax({
               url: 'http://192.168.41.67:8080/security/api/v1/foreign/basic',
               async: false,
               type: 'post',
               contentType: 'application/json',
               dataType: 'json',
               // xhrFields和crossDomain这两个参数是解决跨域的
               xhrFields: {
                   withCredentials: true
               },
               crossDomain: true,
               data: JSON.stringify(params),
               success: function(data){
                   var resultPlat = data;
                   if (resultPlat.status === '200') {
                       userSimProjectCount = resultPlat.data.ROOT.BODY.PRO_NUM; // 项目数
                       userSimScriptCount = resultPlat.data.ROOT.BODY.SCRIPTS_NUM; // 脚本数
                       userSimBugCount = resultPlat.data.ROOT.BODY.DEFECT_NUM; // 缺陷数
                       userSimExecutionCount = resultPlat.data.ROOT.BODY.EXC_NUM; // 执行次数
                       $('#rightProCount').before(userSimProjectCount);
                       $('#rightScriptCount').before(userSimScriptCount);
                       $('#rightBugCount').before(userSimBugCount);
                       $('#rightExeCount').before(userSimExecutionCount);
                   } else {
                       alert('未获取到数据，请联系后台管理员！')
                   }
               },
               error: function(){
               }
           });

           // 计算后获取 项目总数
           var projectTotalCounts = parseInt(publicPlatProjectCount) + parseInt(userSimProjectCount);
           $('#totalProNum').before(projectTotalCounts);

           // 计算后获取 缺陷总数
           var bugTotalCounts = parseInt(publicPlatBugCount) + parseInt(userSimBugCount);
           $('#totalBugNum').before(bugTotalCounts);
       })
   })

  // 跳转至众测平台流程图
 function toPublicPlat() {
    window.open('http://localhost:8181/userPerceptionSystem/publicPlatformFlow.html', '_blank')
  }

  // 跳转至用户模拟平台流程图
  function toUserSimulationFlow() {
    window.open('http://localhost:8181/userPerceptionSystem/userSimulationFlow.html', '_blank')
  }

   // 跳转至众测平台系统
  function toPublicPlatNet() {
    window.open('http://192.168.157.93:8080/#/', '_blank')
  }

   // 跳转至用户模拟平台系统
  function toC() {
    window.open('http://localhost:8181/userPerceptionSystem/c.html', '_blank')
  }
 
