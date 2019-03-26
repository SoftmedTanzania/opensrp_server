<!doctype html>
<html>

<title>OpenSrp WebApp</title>
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


<link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet"
      type="text/css"/>
<link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/Ionicons/css/ionicons.min.css" rel="stylesheet"
      type="${pageContext.request.contextPath}text/css"/>
<link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/jvectormap/jquery-jvectormap.css" rel="stylesheet"
      type="text/css"/>
<link href="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/resources/Dashboard_Files/dist/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/resources/Dashboard_Files/bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet"
      type="text/css"/>



<body class="hold-transition skin-blue sidebar-mini">

<a href="http://23.92.25.157:8081/jasperserver/flow.html?_flowId=viewReportFlow
  &_flowId=viewReportFlow&ParentFolderUri=/reports&reportUnit=/reports/test&standAlone=true">CHW Patients Summary</a>

<div class="wrapper">

    <header class="main-header">

        <!-- Logo -->
        <a class="logo" href="#">
            <!-- mini logo for sidebar mini 50x50 pixels -->
            <span class="logo-mini"><b>O</b>SRP</span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><b>OpenSRP</b>Dashboard</span>
        </a>

        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top">
            <!-- Sidebar toggle button-->
            <a class="sidebar-toggle" data-toggle="push-menu" href="#" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>
        </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- Sidebar user panel -->
            <div class="user-panel">
                <div class="pull-left image">
                </div>
            </div>

            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu" data-widget="tree">
                <li class="header">MAIN NAVIGATION</li>

                <li class="treeview">
                    <a href="#">
                        <i class="fa fa-laptop"></i>
                        <span>Reporting</span>
                        <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="#"><i class="fa fa-circle-o"></i>CHW Patients Summary</a></li>
                    </ul>
                </li>
            </ul>
        </section>
        <!-- /.sidebar -->
    </aside>
</div>

<!--Scripts -->

<!--Online-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"
        type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/resources/vendor/jquery/jquery-3.2.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/animsition/js/animsition.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/bootstrap/js/popper.js"></script>
<script src="${pageContext.request.contextPath}/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
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

<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>


</body>

</html>
