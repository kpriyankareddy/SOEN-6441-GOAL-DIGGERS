var updateStockChart, redirectUser;

$(function() {
  var ws;
  ws = new WebSocket($("body").data("ws-url"));
  ws.onmessage = function(event) {
    var message;
    message = JSON.parse(event.data);
    console.log("Message: "+JSON.parse(event.data));
    switch (message.type) {
      case "stockupdate":
        return updateStockChart(message);
      default:
        return console.log(message);
    }
  };
  return $("#queryForm").submit(function(event) {
    event.preventDefault();
    ws.send(JSON.stringify({
      symbol: $("#queryInput").val()
    }));
    console.log("Sending data from client to server");
    // window.location.href = "http://localhost:9000/home/"+$("#queryInput").val(); // TODO: Step 1: 2. Uncomment to redirect back
    return $("#queryInput").val("");
  });
});

redirectUser = function (ev) {
  console.log(ev);
  // window.location.href =
}

let currentPosts = [];

updateStockChart = function(message) {

  console.log($("#"+message.symbol));
  const post = $(`<section class='submission query' id='${message.symbol}'></section>`);


  if (($("#"+message.symbol).size() !== 0)) {
    $("#"+message.symbol).replaceWith(post);
  }

  const overallSentiment = message.overallSentiment === "HAPPY" ? ":-)" : message.overallSentiment === "SAD" ? ":-(" : ":-|";

  let queryPosts= message.price;
  // queryPosts = queryPosts.sort((a, b) => b.date - a.date);
  // console.log("Sorted posts: "+queryPosts);

  queryPosts = queryPosts.slice(0, Math.min(10, queryPosts.length));

  if(currentPosts.length === 0 || !currentPosts.some(obj => {
    return obj.query === message.symbol
  })){
    currentPosts.unshift({query: message.symbol, queryPosts});
  } else {

    currentPosts = currentPosts.map(obj => {
      if(obj.query === message.symbol)
        obj.queryPosts = queryPosts
      return obj;
    });
  }

    currentPosts.map((allSubmissions) => {
      post.append(` 
 <div class="clear" style="justify-content: space-between">
 <span style="display: inline-block">Search term(s): ${allSubmissions.query}</span>
 <span style="display: inline-block; font-weight: bold">Overall sentiment: ${overallSentiment}</span>
 </div>`);
      allSubmissions.queryPosts.map((submission, id) => {
      post.append(`
<section class="submissionContainer">
<div class="submission_title">
    <span> ${id+1}. </span>
    <span> ${submission["title"]} </span>
    </div>

<div class="submission_subreddit">
                                                <a href="/home/${submission["subreddit"]}">${submission["subreddit"]}</a>
                                            </div>
                                            <div class="submission_link">
                                                <a href="${submission["postLink"]}" target="_blank">${submission["postLink"]}</a>
                                            </div>
                                            <div class="submission_date">
                                                <span>${submission["date"]}</span>
                                            </div>
                                            <div class="submission_author">
                                                <a  
                                                onclick="redirectUser(this)"
                                                id="redirect_${submission["author"]}" href="http://localhost:9000/user/${submission["author"]}">${submission["author"]}</a>
                                            </div>
                                            </section>
`);

});

    });

    $(".container").append(post);
};
