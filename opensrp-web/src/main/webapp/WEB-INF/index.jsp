<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html>

<head>
  <title>OpenSrp WebApp</title>
  <meta charset="UTF-8">

  <meta name="viewport" content="width=device-width, initial-scale=1">
  <%--<link rel="icon" type="image/png" href="/resources/images/icons/favicon.ico">--%>

  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css" />"" />">
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/Dashboard_Files/bower_components/bootstrap/dist/css/bootstrap.css" />" />">

  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/animate/animate.css" />" />">
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/css-hamburgers/hamburgers.min.css" />" />">

  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/animsition/css/animsition.min.css" />" />">
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/select2/select2.min.css" />" />">

  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/daterangepicker/daterangepicker.css" />" />">
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/util.css" />" />">

  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/custom.css" />" />">
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />" />">


  <%--<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">--%>
  <%--<link rel="stylesheet" href="/resources/Dashboard_Files/bower_components/font-awesome/css/font-awesome.min.css">--%>

  <%--<link rel="stylesheet" href="/resources/Dashboard_Files/bower_components/Ionicons/css/ionicons.min.css">--%>
  <%--<link rel="stylesheet" href="/resources/Dashboard_Files/bower_components/jvectormap/jquery-jvectormap.css">--%>
  <%--<link rel="stylesheet" href="/resources/Dashboard_Files/dist/css/AdminLTE.min.css">--%>
  <%--<link rel="stylesheet" href="/resources/Dashboard_Files/dist/css/skins/_all-skins.min.css">--%>
  <%--<link rel="stylesheet" href="/resources/Dashboard_Files/bower_components/bootstrap/dist/css/bootstrap.css">--%>
  <%--<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">--%>

</head>

<body class="hold-transition skin-blue sidebar-mini">

<a href="http://23.92.25.157:8081/jasperserver/flow.html?_flowId=viewReportFlow
  &_flowId=viewReportFlow&ParentFolderUri=/reports&reportUnit=/reports/test&standAlone=true">CHW Summary</a>

<div class="wrapper">

  <header class="main-header">

    <!-- Logo -->
    <a href="#" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><b>O</b>SRP</span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>OpenSRP</b>Dashboard</span>
    </a>

    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
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
            <li><a href="#"><i class="fa fa-circle-o"></i>CHW Summary</a></li>
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
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>

<%--<!--Offline-->--%>
<%--<script src="/resources/vendor/jquery/jquery-3.2.1.min.js"></script>--%>
<%--<script src="/resources/vendor/animsition/js/animsition.min.js"></script>--%>
<%--<script src="/resources/vendor/bootstrap/js/popper.js"></script>--%>
<%--<script src="/resources/vendor/bootstrap/js/bootstrap.min.js" ></script>--%>
<%--<script src="/resources/vendor/select2/select2.min.js"></script>--%>
<%--<script src="/resources/vendor/daterangepicker/moment.min.js"></script>--%>
<%--<script src="/resources/vendor/daterangepicker/daterangepicker.js"></script>--%>
<%--<script src="/resources/vendor/countdowntime/countdowntime.js"></script>--%>
<%--<script src="/resources/js/main.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/bower_components/fastclick/lib/fastclick.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/dist/js/adminlte.min.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/bower_components/jquery-sparkline/dist/jquery.sparkline.min.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/bower_components/chart.js/Chart.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/dist/js/pages/dashboard2.js"></script>--%>
<%--<script src="/resources/Dashboard_Files/dist/js/demo.js"></script>--%>


</body>

</html>
