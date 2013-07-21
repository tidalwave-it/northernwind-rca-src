$(function()
  {
    function clickHijacker (event)
      {
        alert('click:' + event);
        return false;
      }

    $('a').click(clickHijacker);
});
