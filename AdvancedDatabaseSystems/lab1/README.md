
Lab website: <https://upel.agh.edu.pl/weaiib/mod/page/view.php?id=15601>  

# Exercises solutions:  

## Exc 1
All objects are avaliable in [documents.json](documents.json) file.  
To put them into couchdb curl query has been written:  
```curl
curl -X POST $COUCH/pgalczynski -H'content-type: application/json' -d '{ \
>   "date": "2018-03-14", \
>   "subject": "We all gonna die like Hawking did today." \
> }'
```
and so on..  
  
## Exc 2
To create db another curl request has been prepared  
```curl
curl -X PUT $COUCH/pgalczynski
{"ok":true}
```

## Exc 3
Filtering documents for only announcements, object is being checked for "subject" property. To sort by date, simply place doc.date as key in emit function  

function itself:
```javascript
function(doc) {
  if(doc.subject) {
    emit(doc.date, doc);
  }
}
```

## Exc 4
Creating view that sums numer of letter occurances in all objects properties is relativly simple. Implement map that emits value "1" for every letter of its property (name of property and its value), then sum all these 1's in reduce.  

So it goes like this:  
```javascript
// map for summing number of used letters in object props
function(doc) {
  for(var prop in doc) {
    if(doc.hasOwnProperty(prop)) {
      if(prop != '_rev' && prop != '_id') {
        (prop + doc[prop]).replace(/[^a-zA-Z]+/g,'').split('').forEach(function(letter){
          emit(letter.toLowerCase(), 1);
        });
      }
    }
  }
}

//reduce sum of provided letters
function(key, values) {
  return sum(values);
}
```

