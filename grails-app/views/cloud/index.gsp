<html>
<head>
  <title>State Word Cloud</title>
  <script src="/groovystatecloud/static/js/jquery-1.10.2.min.js" type="text/javascript"></script>
  <script src="/groovystatecloud/static/js/jquery.tagcloud.js" type="text/javascript"></script>

  <script type="text/javascript">
    $(document).ready(function() {
      $.fn.tagcloud.defaults = {
        size: {start: 14, end: 18, unit: 'pt'},
        color: {start: '#cde', end: '#f52'}
      };
      $.ajax('/groovystatecloud/cloud/getList').done(function(data) {
        $.each(data, function(index, value) {
          $("#cloud").append('<span rel="' + value[1] + '">' + value[0] + ' </span>');
        });
        $("#cloud span").tagcloud();
      });
    });
   </script>
   <style>
     #cloud {
       margin: 25px auto;
       width: 500px;
     }
   </style>
</head>
<body>
<button type="button" id="fetch">redraw</button><button id="purge" type="button">purge</button>
</ br>
<div id="cloud"></div>
</body>
</html>
