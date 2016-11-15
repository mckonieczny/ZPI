<html>
<head>
</head>
<body>
<h3>Paczki kart</h3>
<p>
    <a href="/panel/decks/create">Dodaj</a>
</p>
<#list decks as deck>
    <div>
        ${deck.id} |
        <#if deck.name??>
            ${deck.name} |
        </#if>
        <#if deck.description??>
            ${deck.description} |
        </#if>
        <a href="/panel/decks/${deck.id}/cards">Edytuj</a>
        <a href="/panel/decks/delete/${deck.id}">Usuń</a>
    </div>
</#list>

</body>
</html>