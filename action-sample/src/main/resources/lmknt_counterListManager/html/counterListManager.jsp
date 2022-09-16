<%@ include file="../../include/import.jspf"%>

<jcr:nodeProperty node="${currentNode}" name="jcr:title" var="title" />
<jcr:nodeProperty node="${currentNode}" name="counterList" var="counterList" />

<h1>${title}</h1>

<div class="counter-list">
    <template:module path="counter-list" node="${counterList.node}" />
</div>