declare namespace util = "http://example.com/util";
 
declare function util:trim($arg as xs:string) as xs:string external;
 
(: a string with surrounding whitespace :)
declare variable $input := "   John Doe    ";
 
<result>{util:trim($input)}</result>
