for $book in fn:doc('books.xml')/books/book 
where xs:decimal($book/price) gt 10.00
return
  $book/title