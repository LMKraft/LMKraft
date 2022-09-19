<%@ include file="../../include/import.jspf"%>

<jcr:nodeProperty node="${currentNode}" name="j:nodename" var="nodename" />
<jcr:nodeProperty node="${currentNode}" name="label" var="label" />
<jcr:nodeProperty node="${currentNode}" name="counter" var="count" />
<jcr:nodeProperty node="${currentNode}" name="jcr:uuid" var="uuid" />

<div class="counter-node">
    <div class="counter-node-info">
        <span class="label">${label} : </span>
        <span class="counter">${count}</span>
    </div>
    <form method="post" action="${url.base}${currentNode.path}.delete.do">
        <input type="hidden" name="jcrRedirectTo" value="${renderContext.mainResource.node.url}" />
        <input type="hidden" name="nodeId" value="${uuid}" />
        <input type="submit" value="Delete" class="${cssDisabledForm}" ${disabledForm}/>
    </form>
</div>

<template:addResources type="css" resources="counterNode.css" />