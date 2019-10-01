<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <head>
        <title>Rates</title>
        <meta name="layout" content="main"/>
    </head>
</head>

<body>
<div class="nav" role="navigation">
    <ul>
        <li>
            <sec:ifLoggedIn>
                <g:form name="logoutForm" controller="logout" action="index">
                    <g:submitButton name="signOut" value="sign out"/>
                </g:form>
            </sec:ifLoggedIn>
        </li>
        <li>
            <sec:ifAnyGranted roles='ROLE_ADMIN'>
                <g:link action="create">Create Rate</g:link>
            </sec:ifAnyGranted>
        </li>
    </ul>
</div>

<div class="content scaffold-list" id="list-rate" role="main">
    <h1>Rate List</h1>
    <table>
        <thead>
        <tr>
            <th class="sortable">
                <h2>Currency</h2>
            </th>
            <th class="sortable">
                <h2>Value</h2>
            </th>
        </tr>
        </thead>
        <tbody>
        <g:each var="rate" in="${rates}">
            <tr class="even">
                <td>${rate.currency}</td>
                <td>${rate.value}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>
<div class="pagination">
    <g:paginate action="rates" total="${rateCount}" max="5" />
</div>
</body>
</html>