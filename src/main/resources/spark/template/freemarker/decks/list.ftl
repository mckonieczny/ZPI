<html>
<head>
</head>
<body>
<h3>Paczki kart</h3>
<p>
    <a href="/panel/decks/create">Dodaj</a>
</p>
<table>
<tr>
    <td>Ulubione</td>
    <td>Nazwa</td>
    <td>Opis</td>
    <td>Trudność</td>
    <td></td>
</tr>
<#list decks as deck>
    <tr>
        <td>
        <#if deck.favorite>
            *
        </#if>
        </td>
        <td>
        <#if deck.name??>
            ${deck.name}
        </#if>
        </td>
        <td>
        <#if deck.description??>
            ${deck.description}
        </#if>
        </td>
        <td>
        <#if deck.difficulty??>
            ${deck.difficulty}
        </#if>
        </td>
        <td><a href="/panel/decks/${deck.id}/cards">Edytuj</a>
            <a href="/panel/decks/delete/${deck.id}">Usuń</a>
            <#if deck.favorite>
                <a href="/panel/favorite/remove/${deck.id}">Usuń z ulubionych</a>
            <#else>
                <a href="/panel/favorite/add/${deck.id}">Dodaj do ulubionych</a>
            </#if>
        </td>
    </tr>
</#list>
</table>
</body>
</html>