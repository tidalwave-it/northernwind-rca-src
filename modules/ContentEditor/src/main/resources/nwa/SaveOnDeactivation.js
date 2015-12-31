aloha.bind('aloha-editable-deactivated', function (event, myEditable)
  {
    $.post("/",
      {
        content: myEditable.editable.getContents()
      });
  });
