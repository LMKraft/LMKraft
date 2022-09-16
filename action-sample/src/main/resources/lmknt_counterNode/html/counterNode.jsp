<%@ include file="../../include/import.jspf"%>

<jcr:nodeProperty node="${currentNode}" name="j:nodename" var="nodename" />
<jcr:nodeProperty node="${currentNode}" name="label" var="label" />
<jcr:nodeProperty node="${currentNode}" name="counter" var="count" />

<div class="counter-node">
    <h2>${label}</h2>
    <span class="counter">${count}</span>
    <input type="submit" value="Delete" />
</div>