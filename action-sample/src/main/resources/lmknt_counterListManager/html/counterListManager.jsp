<%@ include file="../../include/import.jspf"%>

<jcr:nodeProperty node="${currentNode}" name="jcr:title" var="title" />
<jcr:nodeProperty node="${currentNode}" name="counterList" var="counterList" />

<div class="counter-list">
    <h2>${title}</h2>

    <template:module path="counter-list" node="${counterList.node}" />
</div>

<template:addResources type="css" resources="counterListManager.css" />