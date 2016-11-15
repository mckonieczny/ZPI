<html>
<head>
</head>
<body>
<h3>Słówka w paczce {}</h3>
<p>
    <a href="/panel/decks">Powrót</a>
<p/>
<#list cards as card>
    <div>
        ${card.id} |
        <#if card.word??>
            ${card.word} |
        </#if>
        <#if card.translation??>
            ${card.translation} |
        </#if>
        <a href="/panel/decks/${deckId}/cards/delete/${card.id}">Usuń</a>
    </div>
</#list>

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