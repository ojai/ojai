# Generated from antlr4/FieldPathPython.g4 by ANTLR 4.7.1
# encoding: utf-8
from __future__ import print_function
from antlr4 import *
from io import StringIO
import sys


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


def serializedATN():
    with StringIO() as buf:
        buf.write(u"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2")
        buf.write(u"\fs\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t")
        buf.write(u"\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r")
        buf.write(u"\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\4")
        buf.write(u"\23\t\23\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7")
        buf.write(u"\3\7\3\7\3\b\6\b\66\n\b\r\b\16\b\67\3\t\6\t;\n\t\r\t")
        buf.write(u"\16\t<\3\n\3\n\7\nA\n\n\f\n\16\nD\13\n\3\n\3\n\3\n\3")
        buf.write(u"\n\7\nJ\n\n\f\n\16\nM\13\n\3\n\3\n\5\nQ\n\n\3\13\3\13")
        buf.write(u"\3\f\3\f\3\r\3\r\5\rY\n\r\3\16\3\16\5\16]\n\16\3\17\3")
        buf.write(u"\17\3\17\5\17b\n\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22")
        buf.write(u"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\2\2\24")
        buf.write(u"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\2\27\2\31")
        buf.write(u"\2\33\2\35\2\37\2!\2#\2%\f\3\2\b\3\2\62;\5\2\62;CHch")
        buf.write(u"\b\2\n\f\16\17$$\60\60]_bb\7\2\n\f\16\17$$^^bb\6\2$$")
        buf.write(u"\60\61]_bb\7\2ddhhppttvv\2s\2\3\3\2\2\2\2\5\3\2\2\2\2")
        buf.write(u"\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17")
        buf.write(u"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2%\3\2\2\2\3\'\3\2")
        buf.write(u"\2\2\5)\3\2\2\2\7+\3\2\2\2\t-\3\2\2\2\13/\3\2\2\2\r\61")
        buf.write(u"\3\2\2\2\17\65\3\2\2\2\21:\3\2\2\2\23P\3\2\2\2\25R\3")
        buf.write(u"\2\2\2\27T\3\2\2\2\31X\3\2\2\2\33\\\3\2\2\2\35a\3\2\2")
        buf.write(u"\2\37c\3\2\2\2!f\3\2\2\2#i\3\2\2\2%q\3\2\2\2\'(\7\60")
        buf.write(u"\2\2(\4\3\2\2\2)*\7]\2\2*\6\3\2\2\2+,\7_\2\2,\b\3\2\2")
        buf.write(u"\2-.\7$\2\2.\n\3\2\2\2/\60\7b\2\2\60\f\3\2\2\2\61\62")
        buf.write(u"\7\"\2\2\62\63\b\7\2\2\63\16\3\2\2\2\64\66\5\25\13\2")
        buf.write(u"\65\64\3\2\2\2\66\67\3\2\2\2\67\65\3\2\2\2\678\3\2\2")
        buf.write(u"\28\20\3\2\2\29;\5\31\r\2:9\3\2\2\2;<\3\2\2\2<:\3\2\2")
        buf.write(u"\2<=\3\2\2\2=\22\3\2\2\2>B\5\t\5\2?A\5\33\16\2@?\3\2")
        buf.write(u"\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2CE\3\2\2\2DB\3\2\2")
        buf.write(u"\2EF\5\t\5\2FQ\3\2\2\2GK\5\13\6\2HJ\5\33\16\2IH\3\2\2")
        buf.write(u"\2JM\3\2\2\2KI\3\2\2\2KL\3\2\2\2LN\3\2\2\2MK\3\2\2\2")
        buf.write(u"NO\5\13\6\2OQ\3\2\2\2P>\3\2\2\2PG\3\2\2\2Q\24\3\2\2\2")
        buf.write(u"RS\t\2\2\2S\26\3\2\2\2TU\t\3\2\2U\30\3\2\2\2VY\n\4\2")
        buf.write(u"\2WY\5\35\17\2XV\3\2\2\2XW\3\2\2\2Y\32\3\2\2\2Z]\n\5")
        buf.write(u"\2\2[]\5\35\17\2\\Z\3\2\2\2\\[\3\2\2\2]\34\3\2\2\2^b")
        buf.write(u"\5\37\20\2_b\5!\21\2`b\5#\22\2a^\3\2\2\2a_\3\2\2\2a`")
        buf.write(u"\3\2\2\2b\36\3\2\2\2cd\7^\2\2de\t\6\2\2e \3\2\2\2fg\7")
        buf.write(u"^\2\2gh\t\7\2\2h\"\3\2\2\2ij\7^\2\2jk\7w\2\2kl\3\2\2")
        buf.write(u"\2lm\5\27\f\2mn\5\27\f\2no\5\27\f\2op\5\27\f\2p$\3\2")
        buf.write(u"\2\2qr\13\2\2\2r&\3\2\2\2\13\2\67<BKPX\\a\3\3\7\2")
        return buf.getvalue()


class FieldPathPythonLexer(Lexer):

    atn = ATNDeserializer().deserialize(serializedATN())

    decisionsToDFA = [ DFA(ds, i) for i, ds in enumerate(atn.decisionToState) ]

    Period = 1
    OBracket = 2
    CBracket = 3
    DoubleQuote = 4
    BackTick = 5
    Space = 6
    Integer = 7
    Identifier = 8
    QuotedIdentifier = 9
    ErrorChar = 10

    channelNames = [ u"DEFAULT_TOKEN_CHANNEL", u"HIDDEN" ]

    modeNames = [ u"DEFAULT_MODE" ]

    literalNames = [ u"<INVALID>",
            u"'.'", u"'['", u"']'", u"'\"'", u"'`'", u"' '" ]

    symbolicNames = [ u"<INVALID>",
            u"Period", u"OBracket", u"CBracket", u"DoubleQuote", u"BackTick", 
            u"Space", u"Integer", u"Identifier", u"QuotedIdentifier", u"ErrorChar" ]

    ruleNames = [ u"Period", u"OBracket", u"CBracket", u"DoubleQuote", u"BackTick", 
                  u"Space", u"Integer", u"Identifier", u"QuotedIdentifier", 
                  u"Digit", u"HexDigit", u"IdentifierChar", u"QuotedIdentifierChar", 
                  u"EscapeChar", u"SpecialChar", u"ControlChar", u"UnicodeChar", 
                  u"ErrorChar" ]

    grammarFileName = u"FieldPathPython.g4"

    def __init__(self, input=None, output=sys.stdout):
        super(FieldPathPythonLexer, self).__init__(input, output=output)
        self.checkVersion("4.7.1")
        self._interp = LexerATNSimulator(self, self.atn, self.decisionsToDFA, PredictionContextCache())
        self._actions = None
        self._predicates = None


    def action(self, localctx, ruleIndex, actionIndex):
        if self._actions is None:
            actions = dict()
            actions[5] = self.Space_action 
            self._actions = actions
        action = self._actions.get(ruleIndex, None)
        if action is not None:
            action(localctx, actionIndex)
        else:
            raise Exception("No registered action for:" + str(ruleIndex))

    def Space_action(self, localctx , actionIndex):
        if actionIndex == 0:
            pass
     


