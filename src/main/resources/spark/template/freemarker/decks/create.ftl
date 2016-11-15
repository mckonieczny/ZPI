<html>
<head>
</head>
<body>
<h3>Nowa paczka</h3>
<p>
    <a href="/panel/decks/list">Powrót</a>
</p>
<form method="post">
    Nazwa:
    <input type="text" name="name"/>
    <p />
    Opis:
    <textarea name="description"></textarea>
    <p />
    Poziom trudności:
    <input type="number" name="difficulty" min="1" max="5" step="1"/>
    <p />
    <input type="submit" value="Zapisz"/>
</form>
</body>
</html>