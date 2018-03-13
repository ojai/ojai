grammar FieldPathPython;

@header {
"""
  Copyright (c) 2018 MapR, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 """
from ojai.fields.impl.Segment import NameSegment, IndexSegment
from ojai.fields.FieldPath import FieldPath
}

/**
 * Parser rules
 */
parse returns [fp]
: field_segment {$fp = FieldPath($field_segment.seg); }
;

field_segment returns [seg]
: s1 = name_segment {$seg = $s1.seg;}
;

name_segment returns [seg]
: QuotedIdentifier ((Period s1=field_segment) | s2=index_segment)? {
$seg =NameSegment(
      name=NameSegment.un_quote($QuotedIdentifier.text),
      quoted=True,
      child=(None if $s1.start is None and $s2.start is None else $s2.seg if $s1.start is None and $s2.start is not None else $s1.seg));
}
| Identifier ((Period s1=field_segment) | s2=index_segment)? {
$seg =NameSegment(
      name=NameSegment.un_escape($Identifier.text),
      quoted=False,
      child=(None if $s1.start is None and $s2.start is None else $s2.seg if $s1.start is None and $s2.start is not None else $s1.seg));
}
| Integer ((Period s1=field_segment) | s2=index_segment)? {
$seg =NameSegment(
      name=$Integer.text,
      quoted=False,
      child=(None if $s1.start is None and $s2.start is None else $s2.seg if $s1.start is None and $s2.start is not None else $s1.seg));
}
;
index_segment returns [seg]
: OBracket Integer? CBracket ((Period s1=field_segment) | s2=index_segment)? {
$seg =IndexSegment(
        index=$Integer.text,
        child=(None if $s1.start is None and $s2.start is None else $s2.seg if $s1.start is None and $s2.start is not None else $s1.seg));
}
;

/**
 * Lexer rules
 */
Period : '.';
OBracket : '[';
CBracket : ']';
DoubleQuote: '"';
BackTick: '`';

Space
:  ' ' {pass}
;

Integer
:  Digit+
;

Identifier
:  IdentifierChar+
;

QuotedIdentifier
:  DoubleQuote QuotedIdentifierChar* DoubleQuote
|  BackTick QuotedIdentifierChar* BackTick
;

fragment Digit
:  [0-9]
;

fragment HexDigit
:  [0-9a-fA-F]
;

fragment IdentifierChar
:  ~[`"\\\b\f\n\r\t.[\]]
|  EscapeChar
;

fragment QuotedIdentifierChar
:  ~[`"\\\b\f\n\r\t]
|  EscapeChar
;

fragment EscapeChar
:  SpecialChar
|  ControlChar
|  UnicodeChar
;

fragment SpecialChar
:  '\\' [`"\\.[\]/] /* '/' can be appear without escaping */
;

fragment ControlChar
:  '\\' [bfnrt]
;

fragment UnicodeChar
:  '\\u' HexDigit HexDigit HexDigit HexDigit
;

ErrorChar
:  .
;
