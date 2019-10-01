<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Create rate</title>
    <meta name="layout" content="main"/>
</head>

<body>
<h1>Create Rate</h1>
<formset>
    <g:if test="${flash.message}">
        <p style="color: red">${flash.message}</p>
    </g:if>
    <g:form action="newRate">
        <label for="currency">Currency:</label>
        <g:textField name="currency" placeholder="NGN" value="${rate?.currency}"/>
        <g:hasErrors bean="${rate}" field="currency">
            <g:eachError bean="${rate}" field="currency">
                <p style="color: red;"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>
        <label for="value">Value:</label>
        <g:textField name="value" placeholder="0.0025" value="${rate?.value}"/>
        <g:hasErrors bean="${rate}" field="value">
            <g:eachError bean="${rate}" field="value">
                <p style="color: red;"><g:message error="${it}"/></p>
            </g:eachError>
        </g:hasErrors>
        <g:submitButton name="cretae" value="Cretae"/>
    </g:form>
</formset>
</body>
</html>