<html>
<body>

List of all sessions
<table>
  <tr><td>ID</td><td>date</td><td>Action</td><tr>

  % for session,time in sessionNTime:
      <tr>
        <td>${session.id}</td>
        <td>${time}</td>
        <td><a href="/showSession/${session.id}/">replay session</a></td>
      </tr>
  % endfor

</table>

</body>
</html>
