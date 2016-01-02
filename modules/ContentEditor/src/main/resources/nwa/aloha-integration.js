function save()
  {
    console.log('Saving document...');
    var editable = $('.editable')[0];

    $.ajax(
      {
        url: '/',
        data: editable.innerHTML,
        type: 'PUT'
      });
  }

function registerInsertSnippetCommand (editables, name, snippetUrl)
  {
    var command =
      {
        snippetUrl: snippetUrl,
        action: function (boundaries, selection, command, event)
          {
            // FIXME: should better find the parent
            var parent = boundaries[0][0].parentElement;
            $.get(command.snippetUrl, function (snippet)
              {
                console.log("Snippet " + snippet);
                $(parent).append(snippet);
                save();
              });
          }
      };

    var ul = $('#insertSnippetMenu');
    var li = $('<li/>').appendTo(ul);
    var a = $('<a/>').attr('href', '#').text(name).appendTo(li).on('click', aloha.ui.command(editables, command));
  }

function registerInsertSnippetsCommands (editables)
  {
    $.getJSON('/nwa/snippets/list.json', function (snippets)
      {
        snippets.map(function (snippet)
          {
            console.log("Snippet " + snippet.name + " URL " + snippet.url);
            registerInsertSnippetCommand(editables, snippet.name, snippet.url);
          });
      });
  }

(function ()
  {
    'use strict';
    var commands = aloha.maps.merge(aloha.linksUi.commands, aloha.ui.commands);
    var editables = aloha.dom.query('.editable', document).map(aloha);

    for (var selector in commands)
      {
        $('.aloha-action-' + selector).on('click', aloha.ui.command(editables, commands[selector]));
      }

    registerInsertSnippetsCommands(editables);

    function middleware(event)
      {
        console.log('EVENT ' + event.type);

        if ('keyup' === event.type)
          {
            save();
          }

        $('.aloha-ui .active, .aloha-ui.active').removeClass('active');

        if ('leave' === event.type)
         {
            return event;
          }

        var states = aloha.ui.states(commands, event);

        for (var selector in states)
          {
            var $item = $('.aloha-action-' + selector).toggleClass('active', states[selector]);

            if (states[selector] && $item.parents('.dropdown-menu').length)
              {
                $item.closest('.btn-group').find('.dropdown-toggle')
                             .addClass('active')[0].firstChild.data = $item[0].textContent + ' ';
              }
          }

        return event;
      }

    aloha.editor.stack.unshift(aloha.linksUi.middleware, middleware);

    // Because Bootstrap dropdowm menu's use anchor tags containing
    // "href='#'" which causes the page to jump to the top
    $('.aloha-ui .dropdown-menu').on('click', function (event) { event.preventDefault(); });
}());

