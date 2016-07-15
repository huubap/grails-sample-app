<%@ page import="com.yourapp.User" %>
<%@ page import="com.yourapp.Tenant" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
    <script src="http://cdn.auth0.com/js/lock-9.min.js"></script>
    <script src="//cdn.auth0.com/w2/auth0-7.0.3.min.js"></script>
    <script type="text/javascript">
        var auth0 = new Auth0({
            domain:       '${domain}',
            clientID:     '${clientId}',
            state: '${state}',
            callbackURL: "${createLink(controller: 'callback', absolute: true)}",
            scope: 'openid roles user_id name nickname email picture',
        });
        //        $(function () {
        //            $.growl({title: "Welcome!", message: "Please log in"});
        //        });
        function authAjax() {
            auth0.login({
//                      authParams: {
                      // change scopes to whatever you like
                      // claims are added to JWT id_token - openid profile gives everything
                      connection: 'db-conn',
                      username:   $('#username').val(),
                      password:   $('#password').val(),
//                      },
            },
            function (err, result) {
                // store in cookies
                console.log("err", err)
                console.log("result", result)
                console.log("auth0 login callback")
            });
            %{--var lock = new Auth0Lock('${clientId}', '${domain}');--}%
            %{--lock.showSignin({--}%
                %{--authParams: {--}%
                    %{--state: '${state}',--}%
                    %{--// change scopes to whatever you like--}%
                    %{--// claims are added to JWT id_token - openid profile gives everything--}%
                    %{--scope: 'openid roles user_id name nickname email picture'--}%
                %{--},--}%
                %{--callbackURL: "${createLink(controller: 'callback', absolute: true)}",--}%
                %{--responseType: 'code',--}%
                %{--popup: false--}%
            %{--});--}%
        }
    </script>
    <script type='text/javascript'>
        function emptyForm()
        {
            $('#username').val('');
            $('#password').val('');
            $('#remember_me').val('');
        }
    </script>
    <style type="text/css" media="screen">
    #status {
        background-color: #eee;
        border: .2em solid #fff;
        margin: 2em 2em 1em;
        padding: 1em;
        width: 12em;
        float: left;
        -moz-box-shadow: 0px 0px 1.25em #ccc;
        -webkit-box-shadow: 0px 0px 1.25em #ccc;
        box-shadow: 0px 0px 1.25em #ccc;
        -moz-border-radius: 0.6em;
        -webkit-border-radius: 0.6em;
        border-radius: 0.6em;
    }

    .ie6 #status {
        display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
    }

    #status ul {
        font-size: 0.9em;
        list-style-type: none;
        margin-bottom: 0.6em;
        padding: 0;
    }

    #status li {
        line-height: 1.3;
    }

    #status h1 {
        text-transform: uppercase;
        font-size: 1.1em;
        margin: 0 0 0.3em;
    }

    #page-body {
        margin: 2em 1em 1.25em 18em;
    }

    h2 {
        margin-top: 1em;
        margin-bottom: 0.3em;
        font-size: 1em;
    }

    p {
        line-height: 1.5;
        margin: 0.25em 0;
    }

    #controller-list ul {
        list-style-position: inside;
    }

    #controller-list li {
        line-height: 1.3;
        list-style-position: inside;
        margin: 0.25em 0;
    }

    @media screen and (max-width: 480px) {
        #status {
            display: none;
        }

        #page-body {
            margin: 0 1em 1em;
        }

        #page-body h1 {
            margin-top: 0;
        }
    }
    </style>
</head>
<body>
<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div id="status" role="complementary">
    <h1>Application Status</h1>
    <ul>
        <li>App version: <g:meta name="app.version"/></li>
        <li>Grails version: <g:meta name="app.grails.version"/></li>
        <li>Groovy version: ${GroovySystem.getVersion()}</li>
        <li>JVM version: ${System.getProperty('java.version')}</li>
        <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
    </ul>
    <h1>Installed Plugins</h1>
    <ul>
        <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
            <li>${plugin.name} - ${plugin.version}</li>
        </g:each>
    </ul>
</div>
<div id="page-body" role="main">

    <div>
        <g:link controller="protected" action='test'>Protected View</g:link>
    </div>
    <br/>
    <hr/>
    <br/>

    <div id="controller-list" role="navigation">
        <h1>List of tenant:</h1>
        <ul>
            <h2>List of users:</h2>
            <ul>
                <g:each var="u" in="${ User.list() }">
                    <li class="controller">tenant: <b>${u.tenant.code}</b>, username: <b>${u.username}</b>, altUsername: <b>${u.altUsername}</b></li>
                </g:each>
            </ul>
        </ul>
    </div>

    <br/>
    <div>
        Password: pass
    </div>
    <br/>
    <hr/>
    <br/>

    <div id='ajaxLogin'>
        <form  method='POST' id='ajaxLoginForm' name='ajaxLoginForm' >
            <p>
                <label for='tenant'>Tenant</label>
                <input type='text' name='j_tenantCode' id='tenant' />
            </p>
            <p>
                <label for='username'>UserName/AltUsername</label>
                <input type='text' name='j_username' id='username' />
            </p>
            <p>
                <label for='password'>Password</label>
                <input type='password' name='j_password' id='password' />
            </p>
            <p>
                <input type="button" onclick='authAjax(); return false;' value="login" />
            </p>
        </form>
        <div id='errorLoginMsg'></div>
    </div>
</div>
</body>
</html>
