<html>
<head>
  <title>State Word Cloud</title>
</head>
<body>
<g:each in="${tags}">
  <p>State: ${it[0]}</p>
  <p>Weight: ${it[1]}</p>
  </ br>
</g:each>
</body>
</html>
