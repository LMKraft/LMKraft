<%@ include file="../../include/import.jspf"%>

<jcr:nodeProperty node="${currentNode}" name="action" var="action" />
<jcr:nodeProperty node="${currentNode}" name="redirect" var="redirect" />
<c:url var="redirectUrl" value="${redirect.node.url}" />

<div class="submit-form">
    <h2>Submit Form</h2>
    <c:if test="${renderContext.editMode}">
        <ul>
            <li>Called action : ${action}</li>
            <li>Redirects to : ${redirectUrl}</li>
        </ul>
    </c:if>

    <form method="post" action="${url.base}${currentNode.path}.${action}">
        <div class="field">
            <span class="form-label">Field 1</span>
            <input type="text" name="field1" />
        </div>
        <div class="field">
            <span class="form-label">Field 2</span>
            <input type="text" name="field2" />
        </div>
        <div class="field">
            <span class="form-label">Field 3</span>
            <input type="text" name="field3" />
        </div>
        <div class="field submit-btn">
            <input type="submit" value="Send" />
        </div>
    </form>
</div>