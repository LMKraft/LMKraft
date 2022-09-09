
<h1>This Jahia module contains my Action samples.</h1>

<p>
This module contains some Action samples, that you can test using a Submit form. The provided actions are :

* createOrUpdateNode.do : creates or updates a node in a folder
* deleteNode.do : deletes the provided node

You may test those actions by contributing the "Form with Action" component in a new page of your websites, and submitting it.
</p>

<h2>Create or update node</h2>

<p>
Just drop the "Form with action" component in any page. This action only uses the first field of the form.
When you submit the form for the first time, this will create the following path in the contents folder of the current website :
<ul><li>sitekey/contents/counterlist</li></ul>
Inside the "counterlist" will be created a "counterNode", using the first form field for a nodename, and initialized with a counter of 1.
Each time you submit the same label, the node will be updated and incremented by 1.
</p>

<h2>Delete node</h2>
<p>// TODO</p>