<html>
<head>
</head>
<body>
<h3>Słówka w paczce {}</h3>
<p>
    <a href="/panel/decks">Powrót</a>
<p/>
<table>
    <tr>
        <td>Słówko</td>
        <td>Tłumaczenie</td>
        <td></td>
    </tr>
<#list cards as card>
    <tr>
        <td>
        <#if card.word??>
            ${card.word}
        </#if>
        </td>
        <td>
        <#if card.translation??>
            ${card.translation}
        </#if>
        </td>
        <td>
            <a href="/panel/decks/${deckId}/cards/delete/${card.id}">Usuń</a>
        </td>
    </tr>
</#list>
</table>
<p />

<div>
    <form action="/panel/decks/${deckId}/cards/create" method="post">
        <input type="hidden" name="deckId" value="${deckId}"/>
        <div>
            Słówko:
            <input type="text" name="word"/>
        </div>
        <div>
            Tłumaczenie:
            <input type="text" name="translation"/>
        </div>
        <input type="submit" value="Zapisz"/>
    </form>
</div>

</body>
</html>