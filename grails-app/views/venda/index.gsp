<%@ page import="br.com.arrasaamiga.Venda" %>
<%@ page import="br.com.arrasaamiga.StatusVenda" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'venda.label', default: 'Venda')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>

    <asset:javascript src="jquery.js"/>
    <asset:script>
        $(function () {

            $(".formTrackingCode").submit(function () {
                var inputHidden = $(this).find("input[name='trackingCode']");

                var trackingCode = prompt("Código de rastreio: ", $(inputHidden).val());

                if (trackingCode) {
                    $(inputHidden).val(trackingCode);
                } else {
                    return false;
                }
            });

        });
    </asset:script>
</head>

<body>
<a href="#list-venda" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div id="list-venda" class="content scaffold-list" role="main">
    <h1>Vendas</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="cliente" title="Cliente"/>


            <g:sortableColumn property="formaPagamento"
                              title="${message(code: 'venda.formaPagamento.label', default: 'Forma Pagamento')}"/>

            <g:sortableColumn property="status" title="${message(code: 'venda.status.label', default: 'Status')}"/>
            <th>Data</th>
            <th>Opções</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${vendaInstanceList}" status="i" var="vendaInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${vendaInstance.id}">${fieldValue(bean: vendaInstance, field: "cliente.nome")}</g:link></td>


                <td>${fieldValue(bean: vendaInstance, field: "formaPagamento")}</td>

                <td>${fieldValue(bean: vendaInstance, field: "status")}</td>

                <td><g:formatDate format="dd/MM/yyyy" date="${vendaInstance.dateCreated}"/></td>
                <td>
                    <g:if test="${vendaInstance.status == StatusVenda.AguardandoPagamento || vendaInstance.status == StatusVenda.PagamentoRecebido}">

                        <g:form action="marcarEntregue" style="display:inline" id="${vendaInstance?.id}">
                            <g:hiddenField name="offset" value="${params.offset}"/>
                            <g:hiddenField name="max" value="${params.max}"/>
                            <g:actionSubmit action="marcarEntregue" value="Marcar"  onclick="return confirm('Tem certeza');"/>
                        </g:form>

                        <g:form style="display:inline" method="delete" id="${vendaInstance?.id}">
                            <g:hiddenField name="offset" value="${params.offset}"/>
                            <g:hiddenField name="max" value="${params.max}"/>
                            <g:actionSubmit action="delete" value="Excluir" onclick="return confirm('Tem certeza');"/>
                        </g:form>

                        <g:if test="${!vendaInstance.cliente.isDentroDaAreaDeEntregaRapida()}">
                            <g:form class="formTrackingCode" style="display:inline" action="setTrackingCode" id="${vendaInstance?.id}">
                                <g:hiddenField name="offset" value="${params.offset}"/>
                                <g:hiddenField name="max" value="${params.max}"/>
                                <g:hiddenField name="trackingCode" value="${vendaInstance.codigoRastreio}"/>
                                <g:actionSubmit action="setTrackingCode" value="Código de Rastreio"/>
                            </g:form>
                        </g:if>
                    </g:if>
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${vendaInstanceTotal ?: 0}" />
    </div>
</div>
</body>
</html>
