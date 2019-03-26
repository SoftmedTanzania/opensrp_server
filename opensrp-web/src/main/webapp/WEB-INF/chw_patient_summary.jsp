<%--
  Created by IntelliJ IDEA.
  User: Anonymous
  Date: 2019-03-26
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>CHW Patient Summary</title>
    <base href="/opensrp">
    <meta charset="UTF-8">

    <meta content="width=device-width, initial-scale=1" name="viewport">

    <link href="${pageContext.request.contextPath}/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/fonts/Linearicons-Free-v1.0.0/icon-font.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/vendor/animate/animate.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/vendor/css-hamburgers/hamburgers.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/vendor/animsition/css/animsition.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/vendor/select2/select2.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/vendor/daterangepicker/daterangepicker.css" rel="stylesheet" type="text/css"/>

    <link href="${pageContext.request.contextPath}/resources/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/css/util.css" rel="stylesheet" type="text/css"/>

    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

    <link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/Ionicons/css/ionicons.min.css" rel="stylesheet"
          type="${pageContext.request.contextPath}text/css"/>
    <link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/jvectormap/jquery-jvectormap.css" rel="stylesheet"
          type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet"
          type="text/css"/>
</head>
<body>

<%--Report Filters--%>
<div class="row">
    <div class="col-md-12">

        <section class="content">
            <div class="box box-warning">
                <div class="box-header with-border">

                    <div class="col-md-12">

                        <div class="col-md-4">
                            <input type="text" id="dp1" class="span2 datepicker" placeholder="Date..."
                                   name="date">
                        </div>

                        <div class="col-md-4">
                            <input type="text" id="dp2" class="span2 datepicker" placeholder="Date..."
                                   name="date">
                        </div>


                        <div class="col-md-4">
                            <button id="btn_filter_records" class="btn btn-primary">Filter Records</button>
                        </div>


                    </div>

                </div>

            </div>



        </section>

    </div>
</div>

<%--Report content--%>
<div class="row">
    <div class="col-md-12">

        <section class="content">
            <div class="box box-success">
                <div class="box-header with-border">

                    <div id="render_jasper_response">


                    </div>

                </div>

            </div>



        </section>

    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/vendor/jquery/jquery-3.2.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/animsition/js/animsition.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/bootstrap/js/popper.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/select2/select2.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/daterangepicker/moment.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/daterangepicker/daterangepicker.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/countdowntime/countdowntime.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>

<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/fastclick/lib/fastclick.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/js/adminlte.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/jquery-sparkline/dist/jquery.sparkline.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/chart.js/Chart.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/js/pages/dashboard2.js"></script>
<script src="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/js/demo.js"></script>

<!--Online-->
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"
        type="text/javascript"></script>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>

<script>
    $("btn_filter_records").on("click", function (events) {


        $("#id_spinner").modal("show");

        $('#render_jasper_response').load("http://23.92.25.157:8081/jasperserver/flow.html?_flowId=viewReportFlow" +
            "&_flowId=viewReportFlow&ParentFolderUri=%2Freports&reportUnit=%2Freports%2FChwPatientSummaryReport&standAlone=true");


        $("#id_spinner").modal("hide");

    })

    $(function(){
        $('.datepicker').datepicker({
            format: 'mm-dd-yyyy'
        });
    });

</script>

</body>
</html>
