<%@ include file="../../include/import.jspf"%>

<%-- As a list, we've got to include header & footer view, heritated from jmix:list --%>
<template:include view="hidden.header" />

<c:choose>
    <c:when test="${not empty moduleMap.currentList}">
        <c:forEach items="${moduleMap.currentList}" begin="${moduleMap.begin}" end="${moduleMap.end}" var="counterNode">
            <template:module node="${counterNode}" view="default" editable="${moduleMap.editable && !resourceReadOnly}" />
        </c:forEach>
    </c:when>
    <c:otherwise>
        <fmt:message key="lmknt_counterNodeList.empty" />
    </c:otherwise>
</c:choose>

<template:include view="hidden.footer" />