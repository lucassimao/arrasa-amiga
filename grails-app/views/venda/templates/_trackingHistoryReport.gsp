<%@ page import="br.com.arrasaamiga.TrackingHistory" %>
<%@ page contentType="text/html" %>

<!DOCTYPE html>
<head></head>

<body>

<g:each in="${histories}" var="history">
    <div>
        <a target="_blank" style="float: left;"
           href="${createLink(controller: 'venda', action: 'show', id: history.venda.id, absolute: true)}">
            <h3>Venda #${history.venda.id} - ${history.venda.cliente.nome}</h3></a>

        <a style="float: right;" target=" _blank" href="${history.venda.getUrlRastreioCorreios()}">
            <h3>${history.venda.codigoRastreio}</h3></a>
    </div>
    <hr style="clear:both;">
    ${raw(history.history)}
    <p>    <a style="text-align: center;" target=" _blank" href="${createLink(controller: 'venda', action: 'setEncomendaEntregue', id: history.venda.id, absolute: true)}">
        <h4> Marcar como entregue</h4></a>
    </p>
    <hr>
</g:each>
</body>
</html>