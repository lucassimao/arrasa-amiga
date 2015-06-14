<%@ page import="br.com.arrasaamiga.Feriado" %>

<asset:script>
    document.getElementById("descricao").focus();
</asset:script>

<div class="fieldcontain ${hasErrors(bean: feriadoInstance, field: 'descricao', 'error')} required">
    <label for="descricao">
        Descrição        <span class="required-indicator">*</span>
    </label>
    <g:textField id="descricao" name="descricao" size="30" required="" value="${feriadoInstance?.descricao}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: feriadoInstance, field: 'inicio', 'error')} required">
    <label for="inicio">
        Início <span class="required-indicator">*</span>
    </label>
    <g:datePicker name="inicio" precision="day" value="${feriadoInstance?.inicio}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: feriadoInstance, field: 'fim', 'error')} required">
    <label for="fim">
        Fim <span class="required-indicator">*</span>
    </label>
    <g:datePicker name="fim" precision="day" value="${feriadoInstance?.fim}"/>
</div>







