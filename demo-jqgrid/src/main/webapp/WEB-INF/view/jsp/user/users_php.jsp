<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <!-- The jQuery library is a prerequisite for all jqSuite products -->
  <script type="text/ecmascript" src="http://www.guriddo.net/demo/js/jquery.min.js"></script>
  <!-- We support more than 40 localizations -->
  <script type="text/ecmascript" src="http://www.guriddo.net/demo/js/trirand/i18n/grid.locale-en.js"></script>
  <!-- This is the Javascript file of jqGrid -->
  <script type="text/ecmascript" src="http://www.guriddo.net/demo/js/trirand/jquery.jqGrid.min.js"></script>
  <!-- This is the localization file of the grid controlling messages, labels, etc.
  <!-- A link to a jQuery UI ThemeRoller theme, more than 22 built-in and many more custom -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <!-- The link to the CSS that the grid needs -->
  <link rel="stylesheet" type="text/css" media="screen" href="http://www.guriddo.net/demo/css/trirand/ui.jqgrid-bootstrap.css" />
  <script>
      $.jgrid.defaults.width = 780;
      $.jgrid.defaults.styleUI = 'Bootstrap';
  </script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <meta charset="utf-8" />
  <title>jqGrid Loading Data - Virtual mode - paging with scrollbar</title>
</head>
<body>
<div style="margin-left:20px">
  <table id="jqGrid"></table>
  <div id="jqGridPager"></div>
</div>
<script type="text/javascript">

    $(document).ready(function () {
        $("#jqGrid").jqGrid({
            url: 'http://trirand.com/blog/phpjqgrid/examples/jsonp/getjsonp.php?callback=?&qwery=orders',
            mtype: "GET",
            datatype: "jsonp",
            colModel: [
                { label: 'OrderID', name: 'OrderID', key: true, width: 75 },
                { label: 'Customer ID', name: 'CustomerID', width: 150 },
                { label: 'Order Date', name: 'OrderDate', width: 150 },
                { label: 'Freight', name: 'Freight', width: 150 },
                { label:'Ship Name', name: 'ShipName', width: 150 }
            ],
            page: 1,
            width: 780,
            height: 250,
            rowNum: 20,
            scrollPopUp:true,
            scrollLeftOffset: "83%",
            viewrecords: true,
            scroll: 1, // set the scroll property to 1 to enable paging with scrollbar - virtual loading of records
            emptyrecords: 'Scroll to bottom to retrieve new page', // the message will be displayed at the bottom
            pager: "#jqGridPager"
        });
    });

</script>


</body>
</html>