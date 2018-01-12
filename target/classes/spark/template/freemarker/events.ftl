<#import "masterTemplate.ftl" as layout />

<@layout.masterTemplate title="Events">

<div class="row">
    <div class="col-xs-11">
        <h3>${pageTitle}</h3>

   <#if message??>
    	<div class="alert alert-success">
    		${message}
    	</div>
    </#if>
        <#if user??>
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">Create an event ${user.username}!</h3>
                    </div>

                    <div class="panel-body">
                        <form class="form-horizontal" action="/event" method="post">
                            <div class="input-group">
                                <input type="text" name="name" placeholder="What's the name of event?" class="form-control" required/>
                                <input type="text" name="description" placeholder="What is it about?" class="form-control" required/>
                                <input type="date" name="date" placeholder="When is it happening?" class="form-control" required/>
                                <input type="text" name="categories" placeholder="Enter comma separated tags" class="form-control" />
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="submit"> Create </button>
                                </span>
                            </div>
                        </form>
                    </div>
                </div>
        </#if>
    </div>
</div>

<div class="row">
    <div class="col-xs-11">
        <div id="media-list" class="row">
            <#if events??>
                <#list events as event>
                    <hr/>

                    <div class="media">

                        <a class="pull-left" href="/t/${event.name}">
                            <h3>
                                ${event.name}
                            </h3>
                        </a>
                        <div class="media-body">
                            <h4 class="media-heading">
                                <a href="/t/${event.organizer}">
                                ${event.organizer}
                                </a>
                            </h4>

                        ${event.description} <br/>

                            <small>&mdash; ${event.date}</small>
                        </div>

                        <div class="pull-right">
                                       <a class="btn btn-warning" href="/t/${event.name}/going">Going</a>
                                       <a class="btn btn-primary" href="/t/${event.name}/interested">Interested</a>.
                        </div>
                    </div>
                <#else>
                    <hr/>
                    <div class="well">
                        There're no events so far.
                    </div>
                </#list>
            <#else>
                <hr/>
                <div class="well">
                    There're no events so far.
                </div>
            </#if>
        </div>
    </div>
</div>

</@layout.masterTemplate>