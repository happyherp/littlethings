<html>
<body>

List of all sessions
<table>
  <tr><td>ID</td><td>date</td><td>pages opened</td><td>Action</td><tr>

  % for session,time,count in ssnTmCnt:
      <tr>
        <td>${session.id}</td>
        <td>${time}</td>
        <td>${count}</td>
        <td><a href="/showSession/${session.id}/">replay session</a></td>
      </tr>
  % endfor

</table>

</body>
</html>
