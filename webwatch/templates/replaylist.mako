<html>
<body>

List of all replays recorder
<table>
  <tr><td>ID</td><td>Date</td><td>URL</td><td>Action</td><tr>

  % for replay in replays:
      <tr>
        <td>${replay.id}</td>
        <td>${replay.time}</td>
        <td>${replay.url}</td>
        <td><a href="/showReplay/${replay.id}/">Show Replay</a></td>
      </tr>
  % endfor

</table>

</body>
</html>
