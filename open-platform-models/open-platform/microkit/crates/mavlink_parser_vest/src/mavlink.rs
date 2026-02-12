#![allow(warnings)]
#![allow(unused)]
use alloc::vec::Vec;
use vest_lib::bitcoin::varint::{BtcVarint, VarInt};
use vest_lib::properties::*;
use vest_lib::regular::bytes;
use vest_lib::regular::disjoint::DisjointFrom;
use vest_lib::regular::leb128::*;
use vest_lib::regular::modifier::*;
use vest_lib::regular::repetition::*;
use vest_lib::regular::sequence::*;
use vest_lib::regular::tag::*;
use vest_lib::regular::uints::*;
use vest_lib::regular::variant::*;
use vest_lib::utils::*;
use vstd::prelude::*;

macro_rules! impl_wrapper_combinator {
    ($combinator:ty, $combinator_alias:ty) => {
        ::vstd::prelude::verus! {
            impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for $combinator {
                type Type = <$combinator_alias as Combinator<'a, &'a [u8], Vec<u8>>>::Type;
                type SType = <$combinator_alias as Combinator<'a, &'a [u8], Vec<u8>>>::SType;
                fn length(&self, v: Self::SType) -> usize
                { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
                open spec fn ex_requires(&self) -> bool
                { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
                fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
                { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&self.0, s) }
                fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
                { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
            }
        } // verus!
    };
}
verus! {

pub spec const SPEC_ProtocolMagic_MavLink1: u8 = 254;
pub spec const SPEC_ProtocolMagic_MavLink2: u8 = 253;
pub exec static EXEC_ProtocolMagic_MavLink1: u8 ensures EXEC_ProtocolMagic_MavLink1 == SPEC_ProtocolMagic_MavLink1 { 254 }
pub exec static EXEC_ProtocolMagic_MavLink2: u8 ensures EXEC_ProtocolMagic_MavLink2 == SPEC_ProtocolMagic_MavLink2 { 253 }

#[derive(Structural, Debug, Copy, Clone, PartialEq, Eq)]
pub enum ProtocolMagic {
    MavLink1 = 254,
MavLink2 = 253
}
pub type SpecProtocolMagic = ProtocolMagic;

pub type ProtocolMagicInner = u8;

pub type ProtocolMagicInnerRef<'a> = &'a u8;

impl View for ProtocolMagic {
    type V = Self;

    open spec fn view(&self) -> Self::V {
        *self
    }
}

impl SpecTryFrom<ProtocolMagicInner> for ProtocolMagic {
    type Error = ();

    open spec fn spec_try_from(v: ProtocolMagicInner) -> Result<ProtocolMagic, ()> {
        match v {
            254u8 => Ok(ProtocolMagic::MavLink1),
            253u8 => Ok(ProtocolMagic::MavLink2),
            _ => Err(()),
        }
    }
}

impl SpecTryFrom<ProtocolMagic> for ProtocolMagicInner {
    type Error = ();

    open spec fn spec_try_from(v: ProtocolMagic) -> Result<ProtocolMagicInner, ()> {
        match v {
            ProtocolMagic::MavLink1 => Ok(SPEC_ProtocolMagic_MavLink1),
            ProtocolMagic::MavLink2 => Ok(SPEC_ProtocolMagic_MavLink2),
        }
    }
}

impl TryFrom<ProtocolMagicInner> for ProtocolMagic {
    type Error = ();

    fn ex_try_from(v: ProtocolMagicInner) -> Result<ProtocolMagic, ()> {
        match v {
            254u8 => Ok(ProtocolMagic::MavLink1),
            253u8 => Ok(ProtocolMagic::MavLink2),
            _ => Err(()),
        }
    }
}

impl<'a> TryFrom<&'a ProtocolMagic> for ProtocolMagicInnerRef<'a> {
    type Error = ();

    fn ex_try_from(v: &'a ProtocolMagic) -> Result<ProtocolMagicInnerRef<'a>, ()> {
        match v {
            ProtocolMagic::MavLink1 => Ok(&EXEC_ProtocolMagic_MavLink1),
            ProtocolMagic::MavLink2 => Ok(&EXEC_ProtocolMagic_MavLink2),
        }
    }
}

pub struct ProtocolMagicMapper;

impl View for ProtocolMagicMapper {
    type V = Self;

    open spec fn view(&self) -> Self::V {
        *self
    }
}

impl SpecPartialIso for ProtocolMagicMapper {
    type Src = ProtocolMagicInner;
    type Dst = ProtocolMagic;
}

impl SpecPartialIsoProof for ProtocolMagicMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(
            Self::spec_apply(s) matches Ok(v) ==> {
            &&& Self::spec_rev_apply(v) is Ok
            &&& Self::spec_rev_apply(v) matches Ok(s_) && s == s_
        });
    }

    proof fn spec_iso_rev(s: Self::Dst) {
        assert(
            Self::spec_rev_apply(s) matches Ok(v) ==> {
            &&& Self::spec_apply(v) is Ok
            &&& Self::spec_apply(v) matches Ok(s_) && s == s_
        });
    }
}

impl<'a> PartialIso<'a> for ProtocolMagicMapper {
    type Src = ProtocolMagicInner;
    type Dst = ProtocolMagic;
    type RefSrc = ProtocolMagicInnerRef<'a>;
}


pub struct SpecProtocolMagicCombinator(pub SpecProtocolMagicCombinatorAlias);

impl SpecCombinator for SpecProtocolMagicCombinator {
    type Type = SpecProtocolMagic;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecProtocolMagicCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecProtocolMagicCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecProtocolMagicCombinatorAlias = TryMap<U8, ProtocolMagicMapper>;

pub struct ProtocolMagicCombinator(pub ProtocolMagicCombinatorAlias);

impl View for ProtocolMagicCombinator {
    type V = SpecProtocolMagicCombinator;
    open spec fn view(&self) -> Self::V { SpecProtocolMagicCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for ProtocolMagicCombinator {
    type Type = ProtocolMagic;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type ProtocolMagicCombinatorAlias = TryMap<U8, ProtocolMagicMapper>;


pub open spec fn spec_protocol_magic() -> SpecProtocolMagicCombinator {
    SpecProtocolMagicCombinator(TryMap { inner: U8, mapper: ProtocolMagicMapper })
}


pub fn protocol_magic<'a>() -> (o: ProtocolMagicCombinator)
    ensures o@ == spec_protocol_magic(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = ProtocolMagicCombinator(TryMap { inner: U8, mapper: ProtocolMagicMapper });
    assert({
        &&& combinator@ == spec_protocol_magic()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_protocol_magic<'a>(input: &'a [u8]) -> (res: PResult<<ProtocolMagicCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_protocol_magic().spec_parse(input@) == Some((n as int, v@)),
        spec_protocol_magic().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_protocol_magic().spec_parse(input@) is None,
        spec_protocol_magic().spec_parse(input@) is None ==> res is Err,
{
    let combinator = protocol_magic();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_protocol_magic<'a>(v: <ProtocolMagicCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_protocol_magic().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_protocol_magic().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_protocol_magic().spec_serialize(v@))
        },
{
    let combinator = protocol_magic();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn protocol_magic_len<'a>(v: <ProtocolMagicCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_protocol_magic().wf(v@),
        spec_protocol_magic().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_protocol_magic().spec_serialize(v@).len(),
{
    let combinator = protocol_magic();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}


pub mod MessageIdsV1 {
    use super::*;
    pub spec const SPEC_CommandInt: u8 = 75;
    pub spec const SPEC_CommandLong: u8 = 76;
    pub spec const SPEC_CommandAck: u8 = 77;
    pub exec const CommandInt: u8 ensures CommandInt == SPEC_CommandInt { 75 }
    pub exec const CommandLong: u8 ensures CommandLong == SPEC_CommandLong { 76 }
    pub exec const CommandAck: u8 ensures CommandAck == SPEC_CommandAck { 77 }
}


pub struct SpecMessageIdsV1Combinator(pub SpecMessageIdsV1CombinatorAlias);

impl SpecCombinator for SpecMessageIdsV1Combinator {
    type Type = u8;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMessageIdsV1Combinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMessageIdsV1CombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMessageIdsV1CombinatorAlias = U8;

pub struct MessageIdsV1Combinator(pub MessageIdsV1CombinatorAlias);

impl View for MessageIdsV1Combinator {
    type V = SpecMessageIdsV1Combinator;
    open spec fn view(&self) -> Self::V { SpecMessageIdsV1Combinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MessageIdsV1Combinator {
    type Type = u8;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MessageIdsV1CombinatorAlias = U8;


pub open spec fn spec_message_ids_v1() -> SpecMessageIdsV1Combinator {
    SpecMessageIdsV1Combinator(U8)
}


pub fn message_ids_v1<'a>() -> (o: MessageIdsV1Combinator)
    ensures o@ == spec_message_ids_v1(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MessageIdsV1Combinator(U8);
    assert({
        &&& combinator@ == spec_message_ids_v1()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_message_ids_v1<'a>(input: &'a [u8]) -> (res: PResult<<MessageIdsV1Combinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_message_ids_v1().spec_parse(input@) == Some((n as int, v@)),
        spec_message_ids_v1().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_message_ids_v1().spec_parse(input@) is None,
        spec_message_ids_v1().spec_parse(input@) is None ==> res is Err,
{
    let combinator = message_ids_v1();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_message_ids_v1<'a>(v: <MessageIdsV1Combinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_message_ids_v1().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_message_ids_v1().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_message_ids_v1().spec_serialize(v@))
        },
{
    let combinator = message_ids_v1();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn message_ids_v1_len<'a>(v: <MessageIdsV1Combinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_message_ids_v1().wf(v@),
        spec_message_ids_v1().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_message_ids_v1().spec_serialize(v@).len(),
{
    let combinator = message_ids_v1();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}



pub struct SpecMavlinkV1Msg {
    pub len: u8,
    pub seq: u8,
    pub sysid: u8,
    pub compid: u8,
    pub msgid: u8,
    pub payload: Seq<u8>,
    pub checksum: u16,
}

pub type SpecMavlinkV1MsgInner = (u8, (u8, (u8, (u8, (u8, (Seq<u8>, u16))))));


impl SpecFrom<SpecMavlinkV1Msg> for SpecMavlinkV1MsgInner {
    open spec fn spec_from(m: SpecMavlinkV1Msg) -> SpecMavlinkV1MsgInner {
        (m.len, (m.seq, (m.sysid, (m.compid, (m.msgid, (m.payload, m.checksum))))))
    }
}

impl SpecFrom<SpecMavlinkV1MsgInner> for SpecMavlinkV1Msg {
    open spec fn spec_from(m: SpecMavlinkV1MsgInner) -> SpecMavlinkV1Msg {
        let (len, (seq, (sysid, (compid, (msgid, (payload, checksum)))))) = m;
        SpecMavlinkV1Msg { len, seq, sysid, compid, msgid, payload, checksum }
    }
}
#[derive(Debug, Clone, PartialEq, Eq)]

pub struct MavlinkV1Msg<'a> {
    pub len: u8,
    pub seq: u8,
    pub sysid: u8,
    pub compid: u8,
    pub msgid: u8,
    pub payload: &'a [u8],
    pub checksum: u16,
}

impl View for MavlinkV1Msg<'_> {
    type V = SpecMavlinkV1Msg;

    open spec fn view(&self) -> Self::V {
        SpecMavlinkV1Msg {
            len: self.len@,
            seq: self.seq@,
            sysid: self.sysid@,
            compid: self.compid@,
            msgid: self.msgid@,
            payload: self.payload@,
            checksum: self.checksum@,
        }
    }
}
pub type MavlinkV1MsgInner<'a> = (u8, (u8, (u8, (u8, (u8, (&'a [u8], u16))))));

pub type MavlinkV1MsgInnerRef<'a> = (&'a u8, (&'a u8, (&'a u8, (&'a u8, (&'a u8, (&'a &'a [u8], &'a u16))))));
impl<'a> From<&'a MavlinkV1Msg<'a>> for MavlinkV1MsgInnerRef<'a> {
    fn ex_from(m: &'a MavlinkV1Msg) -> MavlinkV1MsgInnerRef<'a> {
        (&m.len, (&m.seq, (&m.sysid, (&m.compid, (&m.msgid, (&m.payload, &m.checksum))))))
    }
}

impl<'a> From<MavlinkV1MsgInner<'a>> for MavlinkV1Msg<'a> {
    fn ex_from(m: MavlinkV1MsgInner) -> MavlinkV1Msg {
        let (len, (seq, (sysid, (compid, (msgid, (payload, checksum)))))) = m;
        MavlinkV1Msg { len, seq, sysid, compid, msgid, payload, checksum }
    }
}

pub struct MavlinkV1MsgMapper;
impl View for MavlinkV1MsgMapper {
    type V = Self;
    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl SpecIso for MavlinkV1MsgMapper {
    type Src = SpecMavlinkV1MsgInner;
    type Dst = SpecMavlinkV1Msg;
}
impl SpecIsoProof for MavlinkV1MsgMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(Self::Src::spec_from(Self::Dst::spec_from(s)) == s);
    }
    proof fn spec_iso_rev(s: Self::Dst) {
        assert(Self::Dst::spec_from(Self::Src::spec_from(s)) == s);
    }
}
impl<'a> Iso<'a> for MavlinkV1MsgMapper {
    type Src = MavlinkV1MsgInner<'a>;
    type Dst = MavlinkV1Msg<'a>;
    type RefSrc = MavlinkV1MsgInnerRef<'a>;
}

pub struct SpecMavlinkV1MsgCombinator(pub SpecMavlinkV1MsgCombinatorAlias);

impl SpecCombinator for SpecMavlinkV1MsgCombinator {
    type Type = SpecMavlinkV1Msg;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMavlinkV1MsgCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMavlinkV1MsgCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMavlinkV1MsgCombinatorAlias = Mapped<SpecPair<U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (SpecMessageIdsV1Combinator, (bytes::Variable, U16Le)))))>, MavlinkV1MsgMapper>;
pub struct Predicate3768926651291043512;
impl View for Predicate3768926651291043512 {
    type V = Self;

    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl Pred<u8> for Predicate3768926651291043512 {
    fn apply(&self, i: &u8) -> bool {
        let i = (*i);
        (i >= 1)
    }
}
impl SpecPred<u8> for Predicate3768926651291043512 {
    open spec fn spec_apply(&self, i: &u8) -> bool {
        let i = (*i);
        (i >= 1)
    }
}

pub struct MavlinkV1MsgCombinator(pub MavlinkV1MsgCombinatorAlias);

impl View for MavlinkV1MsgCombinator {
    type V = SpecMavlinkV1MsgCombinator;
    open spec fn view(&self) -> Self::V { SpecMavlinkV1MsgCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MavlinkV1MsgCombinator {
    type Type = MavlinkV1Msg<'a>;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MavlinkV1MsgCombinatorAlias = Mapped<Pair<U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (MessageIdsV1Combinator, (bytes::Variable, U16Le))))), MavlinkV1MsgCont0>, MavlinkV1MsgMapper>;


pub open spec fn spec_mavlink_v1_msg() -> SpecMavlinkV1MsgCombinator {
    SpecMavlinkV1MsgCombinator(
    Mapped {
        inner: Pair::spec_new(U8, |deps| spec_mavlink_v1_msg_cont0(deps)),
        mapper: MavlinkV1MsgMapper,
    })
}

pub open spec fn spec_mavlink_v1_msg_cont0(deps: u8) -> (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (SpecMessageIdsV1Combinator, (bytes::Variable, U16Le))))) {
    let len = deps;
    (U8, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (spec_message_ids_v1(), (bytes::Variable(len.spec_into()), U16Le)))))
}

impl View for MavlinkV1MsgCont0 {
    type V = spec_fn(u8) -> (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (SpecMessageIdsV1Combinator, (bytes::Variable, U16Le)))));

    open spec fn view(&self) -> Self::V {
        |deps: u8| {
            spec_mavlink_v1_msg_cont0(deps)
        }
    }
}


pub fn mavlink_v1_msg<'a>() -> (o: MavlinkV1MsgCombinator)
    ensures o@ == spec_mavlink_v1_msg(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MavlinkV1MsgCombinator(
    Mapped {
        inner: Pair::new(U8, MavlinkV1MsgCont0),
        mapper: MavlinkV1MsgMapper,
    });
    assert({
        &&& combinator@ == spec_mavlink_v1_msg()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_mavlink_v1_msg<'a>(input: &'a [u8]) -> (res: PResult<<MavlinkV1MsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_mavlink_v1_msg().spec_parse(input@) == Some((n as int, v@)),
        spec_mavlink_v1_msg().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_mavlink_v1_msg().spec_parse(input@) is None,
        spec_mavlink_v1_msg().spec_parse(input@) is None ==> res is Err,
{
    let combinator = mavlink_v1_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_mavlink_v1_msg<'a>(v: <MavlinkV1MsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_mavlink_v1_msg().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_mavlink_v1_msg().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_mavlink_v1_msg().spec_serialize(v@))
        },
{
    let combinator = mavlink_v1_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn mavlink_v1_msg_len<'a>(v: <MavlinkV1MsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_mavlink_v1_msg().wf(v@),
        spec_mavlink_v1_msg().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_mavlink_v1_msg().spec_serialize(v@).len(),
{
    let combinator = mavlink_v1_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}

pub struct MavlinkV1MsgCont0;
type MavlinkV1MsgCont0Type<'a, 'b> = &'b u8;
type MavlinkV1MsgCont0SType<'a, 'x> = &'x u8;
type MavlinkV1MsgCont0Input<'a, 'b, 'x> = POrSType<MavlinkV1MsgCont0Type<'a, 'b>, MavlinkV1MsgCont0SType<'a, 'x>>;
impl<'a, 'b, 'x> Continuation<MavlinkV1MsgCont0Input<'a, 'b, 'x>> for MavlinkV1MsgCont0 {
    type Output = (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (MessageIdsV1Combinator, (bytes::Variable, U16Le)))));

    open spec fn requires(&self, deps: MavlinkV1MsgCont0Input<'a, 'b, 'x>) -> bool { true }

    open spec fn ensures(&self, deps: MavlinkV1MsgCont0Input<'a, 'b, 'x>, o: Self::Output) -> bool {
        o@ == spec_mavlink_v1_msg_cont0(deps@)
    }

    fn apply(&self, deps: MavlinkV1MsgCont0Input<'a, 'b, 'x>) -> Self::Output {
        match deps {
            POrSType::P(deps) => {
                let len = *deps;
                (U8, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (message_ids_v1(), (bytes::Variable(len.ex_into()), U16Le)))))
            }
            POrSType::S(deps) => {
                let len = deps;
                let len = *len;
                (U8, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (message_ids_v1(), (bytes::Variable(len.ex_into()), U16Le)))))
            }
        }
    }
}

pub mod IncompatFlags {
    use super::*;
    pub spec const SPEC_Signed: u8 = 1;
    pub exec const Signed: u8 ensures Signed == SPEC_Signed { 1 }
}


pub struct SpecIncompatFlagsCombinator(pub SpecIncompatFlagsCombinatorAlias);

impl SpecCombinator for SpecIncompatFlagsCombinator {
    type Type = u8;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecIncompatFlagsCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecIncompatFlagsCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecIncompatFlagsCombinatorAlias = U8;

pub struct IncompatFlagsCombinator(pub IncompatFlagsCombinatorAlias);

impl View for IncompatFlagsCombinator {
    type V = SpecIncompatFlagsCombinator;
    open spec fn view(&self) -> Self::V { SpecIncompatFlagsCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for IncompatFlagsCombinator {
    type Type = u8;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type IncompatFlagsCombinatorAlias = U8;


pub open spec fn spec_incompat_flags() -> SpecIncompatFlagsCombinator {
    SpecIncompatFlagsCombinator(U8)
}


pub fn incompat_flags<'a>() -> (o: IncompatFlagsCombinator)
    ensures o@ == spec_incompat_flags(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = IncompatFlagsCombinator(U8);
    assert({
        &&& combinator@ == spec_incompat_flags()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_incompat_flags<'a>(input: &'a [u8]) -> (res: PResult<<IncompatFlagsCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_incompat_flags().spec_parse(input@) == Some((n as int, v@)),
        spec_incompat_flags().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_incompat_flags().spec_parse(input@) is None,
        spec_incompat_flags().spec_parse(input@) is None ==> res is Err,
{
    let combinator = incompat_flags();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_incompat_flags<'a>(v: <IncompatFlagsCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_incompat_flags().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_incompat_flags().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_incompat_flags().spec_serialize(v@))
        },
{
    let combinator = incompat_flags();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn incompat_flags_len<'a>(v: <IncompatFlagsCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_incompat_flags().wf(v@),
        spec_incompat_flags().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_incompat_flags().spec_serialize(v@).len(),
{
    let combinator = incompat_flags();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}


pub mod MessageIdsV2 {
    use super::*;
    pub spec const SPEC_CommandInt: u32 = 75;
    pub spec const SPEC_CommandLong: u32 = 76;
    pub spec const SPEC_CommandAck: u32 = 77;
    pub spec const SPEC_Reserved: u32 = 8388608;
    pub exec const CommandInt: u32 ensures CommandInt == SPEC_CommandInt { 75 }
    pub exec const CommandLong: u32 ensures CommandLong == SPEC_CommandLong { 76 }
    pub exec const CommandAck: u32 ensures CommandAck == SPEC_CommandAck { 77 }
    pub exec const Reserved: u32 ensures Reserved == SPEC_Reserved { 8388608 }
}


pub struct SpecMessageIdsV2Combinator(pub SpecMessageIdsV2CombinatorAlias);

impl SpecCombinator for SpecMessageIdsV2Combinator {
    type Type = u24;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMessageIdsV2Combinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMessageIdsV2CombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMessageIdsV2CombinatorAlias = U24Le;

pub struct MessageIdsV2Combinator(pub MessageIdsV2CombinatorAlias);

impl View for MessageIdsV2Combinator {
    type V = SpecMessageIdsV2Combinator;
    open spec fn view(&self) -> Self::V { SpecMessageIdsV2Combinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MessageIdsV2Combinator {
    type Type = u24;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MessageIdsV2CombinatorAlias = U24Le;


pub open spec fn spec_message_ids_v2() -> SpecMessageIdsV2Combinator {
    SpecMessageIdsV2Combinator(U24Le)
}


pub fn message_ids_v2<'a>() -> (o: MessageIdsV2Combinator)
    ensures o@ == spec_message_ids_v2(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MessageIdsV2Combinator(U24Le);
    assert({
        &&& combinator@ == spec_message_ids_v2()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_message_ids_v2<'a>(input: &'a [u8]) -> (res: PResult<<MessageIdsV2Combinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_message_ids_v2().spec_parse(input@) == Some((n as int, v@)),
        spec_message_ids_v2().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_message_ids_v2().spec_parse(input@) is None,
        spec_message_ids_v2().spec_parse(input@) is None ==> res is Err,
{
    let combinator = message_ids_v2();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_message_ids_v2<'a>(v: <MessageIdsV2Combinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_message_ids_v2().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_message_ids_v2().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_message_ids_v2().spec_serialize(v@))
        },
{
    let combinator = message_ids_v2();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn message_ids_v2_len<'a>(v: <MessageIdsV2Combinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_message_ids_v2().wf(v@),
        spec_message_ids_v2().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_message_ids_v2().spec_serialize(v@).len(),
{
    let combinator = message_ids_v2();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}



pub enum SpecMavlinkV2MsgSignature {
    Variant0(Seq<u8>),
    Variant1(Seq<u8>),
}

pub type SpecMavlinkV2MsgSignatureInner = Either<Seq<u8>, Seq<u8>>;

impl SpecFrom<SpecMavlinkV2MsgSignature> for SpecMavlinkV2MsgSignatureInner {
    open spec fn spec_from(m: SpecMavlinkV2MsgSignature) -> SpecMavlinkV2MsgSignatureInner {
        match m {
            SpecMavlinkV2MsgSignature::Variant0(m) => Either::Left(m),
            SpecMavlinkV2MsgSignature::Variant1(m) => Either::Right(m),
        }
    }

}


impl SpecFrom<SpecMavlinkV2MsgSignatureInner> for SpecMavlinkV2MsgSignature {
    open spec fn spec_from(m: SpecMavlinkV2MsgSignatureInner) -> SpecMavlinkV2MsgSignature {
        match m {
            Either::Left(m) => SpecMavlinkV2MsgSignature::Variant0(m),
            Either::Right(m) => SpecMavlinkV2MsgSignature::Variant1(m),
        }
    }

}



#[derive(Debug, Clone, PartialEq, Eq)]
pub enum MavlinkV2MsgSignature<'a> {
    Variant0(&'a [u8]),
    Variant1(&'a [u8]),
}

pub type MavlinkV2MsgSignatureInner<'a> = Either<&'a [u8], &'a [u8]>;

pub type MavlinkV2MsgSignatureInnerRef<'a> = Either<&'a &'a [u8], &'a &'a [u8]>;


impl<'a> View for MavlinkV2MsgSignature<'a> {
    type V = SpecMavlinkV2MsgSignature;
    open spec fn view(&self) -> Self::V {
        match self {
            MavlinkV2MsgSignature::Variant0(m) => SpecMavlinkV2MsgSignature::Variant0(m@),
            MavlinkV2MsgSignature::Variant1(m) => SpecMavlinkV2MsgSignature::Variant1(m@),
        }
    }
}


impl<'a> From<&'a MavlinkV2MsgSignature<'a>> for MavlinkV2MsgSignatureInnerRef<'a> {
    fn ex_from(m: &'a MavlinkV2MsgSignature<'a>) -> MavlinkV2MsgSignatureInnerRef<'a> {
        match m {
            MavlinkV2MsgSignature::Variant0(m) => Either::Left(m),
            MavlinkV2MsgSignature::Variant1(m) => Either::Right(m),
        }
    }

}

impl<'a> From<MavlinkV2MsgSignatureInner<'a>> for MavlinkV2MsgSignature<'a> {
    fn ex_from(m: MavlinkV2MsgSignatureInner<'a>) -> MavlinkV2MsgSignature<'a> {
        match m {
            Either::Left(m) => MavlinkV2MsgSignature::Variant0(m),
            Either::Right(m) => MavlinkV2MsgSignature::Variant1(m),
        }
    }

}


pub struct MavlinkV2MsgSignatureMapper;
impl View for MavlinkV2MsgSignatureMapper {
    type V = Self;
    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl SpecIso for MavlinkV2MsgSignatureMapper {
    type Src = SpecMavlinkV2MsgSignatureInner;
    type Dst = SpecMavlinkV2MsgSignature;
}
impl SpecIsoProof for MavlinkV2MsgSignatureMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(Self::Src::spec_from(Self::Dst::spec_from(s)) == s);
    }
    proof fn spec_iso_rev(s: Self::Dst) {
        assert(Self::Dst::spec_from(Self::Src::spec_from(s)) == s);
    }
}
impl<'a> Iso<'a> for MavlinkV2MsgSignatureMapper {
    type Src = MavlinkV2MsgSignatureInner<'a>;
    type Dst = MavlinkV2MsgSignature<'a>;
    type RefSrc = MavlinkV2MsgSignatureInnerRef<'a>;
}

type SpecMavlinkV2MsgSignatureCombinatorAlias1 = Choice<Cond<bytes::Fixed<13>>, Cond<bytes::Fixed<0>>>;
pub struct SpecMavlinkV2MsgSignatureCombinator(pub SpecMavlinkV2MsgSignatureCombinatorAlias);

impl SpecCombinator for SpecMavlinkV2MsgSignatureCombinator {
    type Type = SpecMavlinkV2MsgSignature;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMavlinkV2MsgSignatureCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMavlinkV2MsgSignatureCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMavlinkV2MsgSignatureCombinatorAlias = Mapped<SpecMavlinkV2MsgSignatureCombinatorAlias1, MavlinkV2MsgSignatureMapper>;
type MavlinkV2MsgSignatureCombinatorAlias1 = Choice<Cond<bytes::Fixed<13>>, Cond<bytes::Fixed<0>>>;
pub struct MavlinkV2MsgSignatureCombinator1(pub MavlinkV2MsgSignatureCombinatorAlias1);
impl View for MavlinkV2MsgSignatureCombinator1 {
    type V = SpecMavlinkV2MsgSignatureCombinatorAlias1;
    open spec fn view(&self) -> Self::V { self.0@ }
}
impl_wrapper_combinator!(MavlinkV2MsgSignatureCombinator1, MavlinkV2MsgSignatureCombinatorAlias1);

pub struct MavlinkV2MsgSignatureCombinator(pub MavlinkV2MsgSignatureCombinatorAlias);

impl View for MavlinkV2MsgSignatureCombinator {
    type V = SpecMavlinkV2MsgSignatureCombinator;
    open spec fn view(&self) -> Self::V { SpecMavlinkV2MsgSignatureCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MavlinkV2MsgSignatureCombinator {
    type Type = MavlinkV2MsgSignature<'a>;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MavlinkV2MsgSignatureCombinatorAlias = Mapped<MavlinkV2MsgSignatureCombinator1, MavlinkV2MsgSignatureMapper>;


pub open spec fn spec_mavlink_v2_msg_signature(incompat_flags: u8) -> SpecMavlinkV2MsgSignatureCombinator {
    SpecMavlinkV2MsgSignatureCombinator(Mapped { inner: Choice(Cond { cond: incompat_flags == 1, inner: bytes::Fixed::<13> }, Cond { cond: !(incompat_flags == 1), inner: bytes::Fixed::<0> }), mapper: MavlinkV2MsgSignatureMapper })
}

pub fn mavlink_v2_msg_signature<'a>(incompat_flags: u8) -> (o: MavlinkV2MsgSignatureCombinator)
    ensures o@ == spec_mavlink_v2_msg_signature(incompat_flags@),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MavlinkV2MsgSignatureCombinator(Mapped { inner: MavlinkV2MsgSignatureCombinator1(Choice::new(Cond { cond: incompat_flags == 1, inner: bytes::Fixed::<13> }, Cond { cond: !(incompat_flags == 1), inner: bytes::Fixed::<0> })), mapper: MavlinkV2MsgSignatureMapper });
    assert({
        &&& combinator@ == spec_mavlink_v2_msg_signature(incompat_flags@)
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_mavlink_v2_msg_signature<'a>(input: &'a [u8], incompat_flags: u8) -> (res: PResult<<MavlinkV2MsgSignatureCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_mavlink_v2_msg_signature(incompat_flags@).spec_parse(input@) == Some((n as int, v@)),
        spec_mavlink_v2_msg_signature(incompat_flags@).spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_mavlink_v2_msg_signature(incompat_flags@).spec_parse(input@) is None,
        spec_mavlink_v2_msg_signature(incompat_flags@).spec_parse(input@) is None ==> res is Err,
{
    let combinator = mavlink_v2_msg_signature( incompat_flags );
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_mavlink_v2_msg_signature<'a>(v: <MavlinkV2MsgSignatureCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize, incompat_flags: u8) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_mavlink_v2_msg_signature(incompat_flags@).wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_mavlink_v2_msg_signature(incompat_flags@).spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_mavlink_v2_msg_signature(incompat_flags@).spec_serialize(v@))
        },
{
    let combinator = mavlink_v2_msg_signature( incompat_flags );
    combinator.serialize(v, data, pos)
}

pub fn mavlink_v2_msg_signature_len<'a>(v: <MavlinkV2MsgSignatureCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, incompat_flags: u8) -> (serialize_len: usize)
    requires
        spec_mavlink_v2_msg_signature(incompat_flags@).wf(v@),
        spec_mavlink_v2_msg_signature(incompat_flags@).spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_mavlink_v2_msg_signature(incompat_flags@).spec_serialize(v@).len(),
{
    let combinator = mavlink_v2_msg_signature( incompat_flags );
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}


pub struct SpecMavlinkV2Msg {
    pub len: u8,
    pub incompat_flags: u8,
    pub compat_flags: u8,
    pub seq: u8,
    pub sysid: u8,
    pub compid: u8,
    pub msgid: u24,
    pub payload: Seq<u8>,
    pub checksum: u16,
    pub signature: SpecMavlinkV2MsgSignature,
}

pub type SpecMavlinkV2MsgInner = ((u8, u8), (u8, (u8, (u8, (u8, (u24, (Seq<u8>, (u16, SpecMavlinkV2MsgSignature))))))));


impl SpecFrom<SpecMavlinkV2Msg> for SpecMavlinkV2MsgInner {
    open spec fn spec_from(m: SpecMavlinkV2Msg) -> SpecMavlinkV2MsgInner {
        ((m.len, m.incompat_flags), (m.compat_flags, (m.seq, (m.sysid, (m.compid, (m.msgid, (m.payload, (m.checksum, m.signature))))))))
    }
}

impl SpecFrom<SpecMavlinkV2MsgInner> for SpecMavlinkV2Msg {
    open spec fn spec_from(m: SpecMavlinkV2MsgInner) -> SpecMavlinkV2Msg {
        let ((len, incompat_flags), (compat_flags, (seq, (sysid, (compid, (msgid, (payload, (checksum, signature)))))))) = m;
        SpecMavlinkV2Msg { len, incompat_flags, compat_flags, seq, sysid, compid, msgid, payload, checksum, signature }
    }
}
#[derive(Debug, Clone, PartialEq, Eq)]

pub struct MavlinkV2Msg<'a> {
    pub len: u8,
    pub incompat_flags: u8,
    pub compat_flags: u8,
    pub seq: u8,
    pub sysid: u8,
    pub compid: u8,
    pub msgid: u24,
    pub payload: &'a [u8],
    pub checksum: u16,
    pub signature: MavlinkV2MsgSignature<'a>,
}

impl View for MavlinkV2Msg<'_> {
    type V = SpecMavlinkV2Msg;

    open spec fn view(&self) -> Self::V {
        SpecMavlinkV2Msg {
            len: self.len@,
            incompat_flags: self.incompat_flags@,
            compat_flags: self.compat_flags@,
            seq: self.seq@,
            sysid: self.sysid@,
            compid: self.compid@,
            msgid: self.msgid@,
            payload: self.payload@,
            checksum: self.checksum@,
            signature: self.signature@,
        }
    }
}
pub type MavlinkV2MsgInner<'a> = ((u8, u8), (u8, (u8, (u8, (u8, (u24, (&'a [u8], (u16, MavlinkV2MsgSignature<'a>))))))));

pub type MavlinkV2MsgInnerRef<'a> = ((&'a u8, &'a u8), (&'a u8, (&'a u8, (&'a u8, (&'a u8, (&'a u24, (&'a &'a [u8], (&'a u16, &'a MavlinkV2MsgSignature<'a>))))))));
impl<'a> From<&'a MavlinkV2Msg<'a>> for MavlinkV2MsgInnerRef<'a> {
    fn ex_from(m: &'a MavlinkV2Msg) -> MavlinkV2MsgInnerRef<'a> {
        ((&m.len, &m.incompat_flags), (&m.compat_flags, (&m.seq, (&m.sysid, (&m.compid, (&m.msgid, (&m.payload, (&m.checksum, &m.signature))))))))
    }
}

impl<'a> From<MavlinkV2MsgInner<'a>> for MavlinkV2Msg<'a> {
    fn ex_from(m: MavlinkV2MsgInner) -> MavlinkV2Msg {
        let ((len, incompat_flags), (compat_flags, (seq, (sysid, (compid, (msgid, (payload, (checksum, signature)))))))) = m;
        MavlinkV2Msg { len, incompat_flags, compat_flags, seq, sysid, compid, msgid, payload, checksum, signature }
    }
}

pub struct MavlinkV2MsgMapper;
impl View for MavlinkV2MsgMapper {
    type V = Self;
    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl SpecIso for MavlinkV2MsgMapper {
    type Src = SpecMavlinkV2MsgInner;
    type Dst = SpecMavlinkV2Msg;
}
impl SpecIsoProof for MavlinkV2MsgMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(Self::Src::spec_from(Self::Dst::spec_from(s)) == s);
    }
    proof fn spec_iso_rev(s: Self::Dst) {
        assert(Self::Dst::spec_from(Self::Src::spec_from(s)) == s);
    }
}
impl<'a> Iso<'a> for MavlinkV2MsgMapper {
    type Src = MavlinkV2MsgInner<'a>;
    type Dst = MavlinkV2Msg<'a>;
    type RefSrc = MavlinkV2MsgInnerRef<'a>;
}

pub struct SpecMavlinkV2MsgCombinator(pub SpecMavlinkV2MsgCombinatorAlias);

impl SpecCombinator for SpecMavlinkV2MsgCombinator {
    type Type = SpecMavlinkV2Msg;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMavlinkV2MsgCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMavlinkV2MsgCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMavlinkV2MsgCombinatorAlias = Mapped<SpecPair<SpecPair<U8, SpecIncompatFlagsCombinator>, (U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (SpecMessageIdsV2Combinator, (bytes::Variable, (U16Le, SpecMavlinkV2MsgSignatureCombinator)))))))>, MavlinkV2MsgMapper>;

pub struct MavlinkV2MsgCombinator(pub MavlinkV2MsgCombinatorAlias);

impl View for MavlinkV2MsgCombinator {
    type V = SpecMavlinkV2MsgCombinator;
    open spec fn view(&self) -> Self::V { SpecMavlinkV2MsgCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MavlinkV2MsgCombinator {
    type Type = MavlinkV2Msg<'a>;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MavlinkV2MsgCombinatorAlias = Mapped<Pair<Pair<U8, IncompatFlagsCombinator, MavlinkV2MsgCont1>, (U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (MessageIdsV2Combinator, (bytes::Variable, (U16Le, MavlinkV2MsgSignatureCombinator))))))), MavlinkV2MsgCont0>, MavlinkV2MsgMapper>;


pub open spec fn spec_mavlink_v2_msg() -> SpecMavlinkV2MsgCombinator {
    SpecMavlinkV2MsgCombinator(
    Mapped {
        inner: Pair::spec_new(Pair::spec_new(U8, |deps| spec_mavlink_v2_msg_cont1(deps)), |deps| spec_mavlink_v2_msg_cont0(deps)),
        mapper: MavlinkV2MsgMapper,
    })
}

pub open spec fn spec_mavlink_v2_msg_cont1(deps: u8) -> SpecIncompatFlagsCombinator {
    let len = deps;
    spec_incompat_flags()
}

impl View for MavlinkV2MsgCont1 {
    type V = spec_fn(u8) -> SpecIncompatFlagsCombinator;

    open spec fn view(&self) -> Self::V {
        |deps: u8| {
            spec_mavlink_v2_msg_cont1(deps)
        }
    }
}

pub open spec fn spec_mavlink_v2_msg_cont0(deps: (u8, u8)) -> (U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (SpecMessageIdsV2Combinator, (bytes::Variable, (U16Le, SpecMavlinkV2MsgSignatureCombinator))))))) {
    let (len, incompat_flags) = deps;
    (U8, (U8, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (spec_message_ids_v2(), (bytes::Variable(len.spec_into()), (U16Le, spec_mavlink_v2_msg_signature(incompat_flags))))))))
}

impl View for MavlinkV2MsgCont0 {
    type V = spec_fn((u8, u8)) -> (U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (SpecMessageIdsV2Combinator, (bytes::Variable, (U16Le, SpecMavlinkV2MsgSignatureCombinator)))))));

    open spec fn view(&self) -> Self::V {
        |deps: (u8, u8)| {
            spec_mavlink_v2_msg_cont0(deps)
        }
    }
}


pub fn mavlink_v2_msg<'a>() -> (o: MavlinkV2MsgCombinator)
    ensures o@ == spec_mavlink_v2_msg(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MavlinkV2MsgCombinator(
    Mapped {
        inner: Pair::new(Pair::new(U8, MavlinkV2MsgCont1), MavlinkV2MsgCont0),
        mapper: MavlinkV2MsgMapper,
    });
    assert({
        &&& combinator@ == spec_mavlink_v2_msg()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_mavlink_v2_msg<'a>(input: &'a [u8]) -> (res: PResult<<MavlinkV2MsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_mavlink_v2_msg().spec_parse(input@) == Some((n as int, v@)),
        spec_mavlink_v2_msg().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_mavlink_v2_msg().spec_parse(input@) is None,
        spec_mavlink_v2_msg().spec_parse(input@) is None ==> res is Err,
{
    let combinator = mavlink_v2_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_mavlink_v2_msg<'a>(v: <MavlinkV2MsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_mavlink_v2_msg().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_mavlink_v2_msg().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_mavlink_v2_msg().spec_serialize(v@))
        },
{
    let combinator = mavlink_v2_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn mavlink_v2_msg_len<'a>(v: <MavlinkV2MsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_mavlink_v2_msg().wf(v@),
        spec_mavlink_v2_msg().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_mavlink_v2_msg().spec_serialize(v@).len(),
{
    let combinator = mavlink_v2_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}

pub struct MavlinkV2MsgCont1;
type MavlinkV2MsgCont1Type<'a, 'b> = &'b u8;
type MavlinkV2MsgCont1SType<'a, 'x> = &'x u8;
type MavlinkV2MsgCont1Input<'a, 'b, 'x> = POrSType<MavlinkV2MsgCont1Type<'a, 'b>, MavlinkV2MsgCont1SType<'a, 'x>>;
impl<'a, 'b, 'x> Continuation<MavlinkV2MsgCont1Input<'a, 'b, 'x>> for MavlinkV2MsgCont1 {
    type Output = IncompatFlagsCombinator;

    open spec fn requires(&self, deps: MavlinkV2MsgCont1Input<'a, 'b, 'x>) -> bool { true }

    open spec fn ensures(&self, deps: MavlinkV2MsgCont1Input<'a, 'b, 'x>, o: Self::Output) -> bool {
        o@ == spec_mavlink_v2_msg_cont1(deps@)
    }

    fn apply(&self, deps: MavlinkV2MsgCont1Input<'a, 'b, 'x>) -> Self::Output {
        match deps {
            POrSType::P(deps) => {
                let len = *deps;
                incompat_flags()
            }
            POrSType::S(deps) => {
                let len = deps;
                let len = *len;
                incompat_flags()
            }
        }
    }
}
pub struct MavlinkV2MsgCont0;
type MavlinkV2MsgCont0Type<'a, 'b> = &'b (u8, u8);
type MavlinkV2MsgCont0SType<'a, 'x> = (&'x u8, &'x u8);
type MavlinkV2MsgCont0Input<'a, 'b, 'x> = POrSType<MavlinkV2MsgCont0Type<'a, 'b>, MavlinkV2MsgCont0SType<'a, 'x>>;
impl<'a, 'b, 'x> Continuation<MavlinkV2MsgCont0Input<'a, 'b, 'x>> for MavlinkV2MsgCont0 {
    type Output = (U8, (U8, (Refined<U8, Predicate3768926651291043512>, (Refined<U8, Predicate3768926651291043512>, (MessageIdsV2Combinator, (bytes::Variable, (U16Le, MavlinkV2MsgSignatureCombinator)))))));

    open spec fn requires(&self, deps: MavlinkV2MsgCont0Input<'a, 'b, 'x>) -> bool { true }

    open spec fn ensures(&self, deps: MavlinkV2MsgCont0Input<'a, 'b, 'x>, o: Self::Output) -> bool {
        o@ == spec_mavlink_v2_msg_cont0(deps@)
    }

    fn apply(&self, deps: MavlinkV2MsgCont0Input<'a, 'b, 'x>) -> Self::Output {
        match deps {
            POrSType::P(deps) => {
                let (len, incompat_flags) = *deps;
                (U8, (U8, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (message_ids_v2(), (bytes::Variable(len.ex_into()), (U16Le, mavlink_v2_msg_signature(incompat_flags))))))))
            }
            POrSType::S(deps) => {
                let (len, incompat_flags) = deps;
                let (len, incompat_flags) = (*len, *incompat_flags);
                (U8, (U8, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (Refined { inner: U8, predicate: Predicate3768926651291043512 }, (message_ids_v2(), (bytes::Variable(len.ex_into()), (U16Le, mavlink_v2_msg_signature(incompat_flags))))))))
            }
        }
    }
}


pub enum SpecMavlinkMsgMsg {
    MavLink1(SpecMavlinkV1Msg),
    MavLink2(SpecMavlinkV2Msg),
}

pub type SpecMavlinkMsgMsgInner = Either<SpecMavlinkV1Msg, SpecMavlinkV2Msg>;

impl SpecFrom<SpecMavlinkMsgMsg> for SpecMavlinkMsgMsgInner {
    open spec fn spec_from(m: SpecMavlinkMsgMsg) -> SpecMavlinkMsgMsgInner {
        match m {
            SpecMavlinkMsgMsg::MavLink1(m) => Either::Left(m),
            SpecMavlinkMsgMsg::MavLink2(m) => Either::Right(m),
        }
    }

}


impl SpecFrom<SpecMavlinkMsgMsgInner> for SpecMavlinkMsgMsg {
    open spec fn spec_from(m: SpecMavlinkMsgMsgInner) -> SpecMavlinkMsgMsg {
        match m {
            Either::Left(m) => SpecMavlinkMsgMsg::MavLink1(m),
            Either::Right(m) => SpecMavlinkMsgMsg::MavLink2(m),
        }
    }

}



#[derive(Debug, Clone, PartialEq, Eq)]
pub enum MavlinkMsgMsg<'a> {
    MavLink1(MavlinkV1Msg<'a>),
    MavLink2(MavlinkV2Msg<'a>),
}

pub type MavlinkMsgMsgInner<'a> = Either<MavlinkV1Msg<'a>, MavlinkV2Msg<'a>>;

pub type MavlinkMsgMsgInnerRef<'a> = Either<&'a MavlinkV1Msg<'a>, &'a MavlinkV2Msg<'a>>;


impl<'a> View for MavlinkMsgMsg<'a> {
    type V = SpecMavlinkMsgMsg;
    open spec fn view(&self) -> Self::V {
        match self {
            MavlinkMsgMsg::MavLink1(m) => SpecMavlinkMsgMsg::MavLink1(m@),
            MavlinkMsgMsg::MavLink2(m) => SpecMavlinkMsgMsg::MavLink2(m@),
        }
    }
}


impl<'a> From<&'a MavlinkMsgMsg<'a>> for MavlinkMsgMsgInnerRef<'a> {
    fn ex_from(m: &'a MavlinkMsgMsg<'a>) -> MavlinkMsgMsgInnerRef<'a> {
        match m {
            MavlinkMsgMsg::MavLink1(m) => Either::Left(m),
            MavlinkMsgMsg::MavLink2(m) => Either::Right(m),
        }
    }

}

impl<'a> From<MavlinkMsgMsgInner<'a>> for MavlinkMsgMsg<'a> {
    fn ex_from(m: MavlinkMsgMsgInner<'a>) -> MavlinkMsgMsg<'a> {
        match m {
            Either::Left(m) => MavlinkMsgMsg::MavLink1(m),
            Either::Right(m) => MavlinkMsgMsg::MavLink2(m),
        }
    }

}


pub struct MavlinkMsgMsgMapper;
impl View for MavlinkMsgMsgMapper {
    type V = Self;
    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl SpecIso for MavlinkMsgMsgMapper {
    type Src = SpecMavlinkMsgMsgInner;
    type Dst = SpecMavlinkMsgMsg;
}
impl SpecIsoProof for MavlinkMsgMsgMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(Self::Src::spec_from(Self::Dst::spec_from(s)) == s);
    }
    proof fn spec_iso_rev(s: Self::Dst) {
        assert(Self::Dst::spec_from(Self::Src::spec_from(s)) == s);
    }
}
impl<'a> Iso<'a> for MavlinkMsgMsgMapper {
    type Src = MavlinkMsgMsgInner<'a>;
    type Dst = MavlinkMsgMsg<'a>;
    type RefSrc = MavlinkMsgMsgInnerRef<'a>;
}

type SpecMavlinkMsgMsgCombinatorAlias1 = Choice<Cond<SpecMavlinkV1MsgCombinator>, Cond<SpecMavlinkV2MsgCombinator>>;
pub struct SpecMavlinkMsgMsgCombinator(pub SpecMavlinkMsgMsgCombinatorAlias);

impl SpecCombinator for SpecMavlinkMsgMsgCombinator {
    type Type = SpecMavlinkMsgMsg;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMavlinkMsgMsgCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMavlinkMsgMsgCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMavlinkMsgMsgCombinatorAlias = Mapped<SpecMavlinkMsgMsgCombinatorAlias1, MavlinkMsgMsgMapper>;
type MavlinkMsgMsgCombinatorAlias1 = Choice<Cond<MavlinkV1MsgCombinator>, Cond<MavlinkV2MsgCombinator>>;
pub struct MavlinkMsgMsgCombinator1(pub MavlinkMsgMsgCombinatorAlias1);
impl View for MavlinkMsgMsgCombinator1 {
    type V = SpecMavlinkMsgMsgCombinatorAlias1;
    open spec fn view(&self) -> Self::V { self.0@ }
}
impl_wrapper_combinator!(MavlinkMsgMsgCombinator1, MavlinkMsgMsgCombinatorAlias1);

pub struct MavlinkMsgMsgCombinator(pub MavlinkMsgMsgCombinatorAlias);

impl View for MavlinkMsgMsgCombinator {
    type V = SpecMavlinkMsgMsgCombinator;
    open spec fn view(&self) -> Self::V { SpecMavlinkMsgMsgCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MavlinkMsgMsgCombinator {
    type Type = MavlinkMsgMsg<'a>;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MavlinkMsgMsgCombinatorAlias = Mapped<MavlinkMsgMsgCombinator1, MavlinkMsgMsgMapper>;


pub open spec fn spec_mavlink_msg_msg(magic: SpecProtocolMagic) -> SpecMavlinkMsgMsgCombinator {
    SpecMavlinkMsgMsgCombinator(Mapped { inner: Choice(Cond { cond: magic == ProtocolMagic::MavLink1, inner: spec_mavlink_v1_msg() }, Cond { cond: magic == ProtocolMagic::MavLink2, inner: spec_mavlink_v2_msg() }), mapper: MavlinkMsgMsgMapper })
}

pub fn mavlink_msg_msg<'a>(magic: ProtocolMagic) -> (o: MavlinkMsgMsgCombinator)
    ensures o@ == spec_mavlink_msg_msg(magic@),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MavlinkMsgMsgCombinator(Mapped { inner: MavlinkMsgMsgCombinator1(Choice::new(Cond { cond: magic == ProtocolMagic::MavLink1, inner: mavlink_v1_msg() }, Cond { cond: magic == ProtocolMagic::MavLink2, inner: mavlink_v2_msg() })), mapper: MavlinkMsgMsgMapper });
    assert({
        &&& combinator@ == spec_mavlink_msg_msg(magic@)
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_mavlink_msg_msg<'a>(input: &'a [u8], magic: ProtocolMagic) -> (res: PResult<<MavlinkMsgMsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_mavlink_msg_msg(magic@).spec_parse(input@) == Some((n as int, v@)),
        spec_mavlink_msg_msg(magic@).spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_mavlink_msg_msg(magic@).spec_parse(input@) is None,
        spec_mavlink_msg_msg(magic@).spec_parse(input@) is None ==> res is Err,
{
    let combinator = mavlink_msg_msg( magic );
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_mavlink_msg_msg<'a>(v: <MavlinkMsgMsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize, magic: ProtocolMagic) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_mavlink_msg_msg(magic@).wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_mavlink_msg_msg(magic@).spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_mavlink_msg_msg(magic@).spec_serialize(v@))
        },
{
    let combinator = mavlink_msg_msg( magic );
    combinator.serialize(v, data, pos)
}

pub fn mavlink_msg_msg_len<'a>(v: <MavlinkMsgMsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, magic: ProtocolMagic) -> (serialize_len: usize)
    requires
        spec_mavlink_msg_msg(magic@).wf(v@),
        spec_mavlink_msg_msg(magic@).spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_mavlink_msg_msg(magic@).spec_serialize(v@).len(),
{
    let combinator = mavlink_msg_msg( magic );
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}


pub struct SpecMavlinkMsg {
    pub magic: SpecProtocolMagic,
    pub msg: SpecMavlinkMsgMsg,
}

pub type SpecMavlinkMsgInner = (SpecProtocolMagic, SpecMavlinkMsgMsg);


impl SpecFrom<SpecMavlinkMsg> for SpecMavlinkMsgInner {
    open spec fn spec_from(m: SpecMavlinkMsg) -> SpecMavlinkMsgInner {
        (m.magic, m.msg)
    }
}

impl SpecFrom<SpecMavlinkMsgInner> for SpecMavlinkMsg {
    open spec fn spec_from(m: SpecMavlinkMsgInner) -> SpecMavlinkMsg {
        let (magic, msg) = m;
        SpecMavlinkMsg { magic, msg }
    }
}
#[derive(Debug, Clone, PartialEq, Eq)]

pub struct MavlinkMsg<'a> {
    pub magic: ProtocolMagic,
    pub msg: MavlinkMsgMsg<'a>,
}

impl View for MavlinkMsg<'_> {
    type V = SpecMavlinkMsg;

    open spec fn view(&self) -> Self::V {
        SpecMavlinkMsg {
            magic: self.magic@,
            msg: self.msg@,
        }
    }
}
pub type MavlinkMsgInner<'a> = (ProtocolMagic, MavlinkMsgMsg<'a>);

pub type MavlinkMsgInnerRef<'a> = (&'a ProtocolMagic, &'a MavlinkMsgMsg<'a>);
impl<'a> From<&'a MavlinkMsg<'a>> for MavlinkMsgInnerRef<'a> {
    fn ex_from(m: &'a MavlinkMsg) -> MavlinkMsgInnerRef<'a> {
        (&m.magic, &m.msg)
    }
}

impl<'a> From<MavlinkMsgInner<'a>> for MavlinkMsg<'a> {
    fn ex_from(m: MavlinkMsgInner) -> MavlinkMsg {
        let (magic, msg) = m;
        MavlinkMsg { magic, msg }
    }
}

pub struct MavlinkMsgMapper;
impl View for MavlinkMsgMapper {
    type V = Self;
    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl SpecIso for MavlinkMsgMapper {
    type Src = SpecMavlinkMsgInner;
    type Dst = SpecMavlinkMsg;
}
impl SpecIsoProof for MavlinkMsgMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(Self::Src::spec_from(Self::Dst::spec_from(s)) == s);
    }
    proof fn spec_iso_rev(s: Self::Dst) {
        assert(Self::Dst::spec_from(Self::Src::spec_from(s)) == s);
    }
}
impl<'a> Iso<'a> for MavlinkMsgMapper {
    type Src = MavlinkMsgInner<'a>;
    type Dst = MavlinkMsg<'a>;
    type RefSrc = MavlinkMsgInnerRef<'a>;
}

pub struct SpecMavlinkMsgCombinator(pub SpecMavlinkMsgCombinatorAlias);

impl SpecCombinator for SpecMavlinkMsgCombinator {
    type Type = SpecMavlinkMsg;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMavlinkMsgCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMavlinkMsgCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMavlinkMsgCombinatorAlias = Mapped<SpecPair<SpecProtocolMagicCombinator, SpecMavlinkMsgMsgCombinator>, MavlinkMsgMapper>;

pub struct MavlinkMsgCombinator(pub MavlinkMsgCombinatorAlias);

impl View for MavlinkMsgCombinator {
    type V = SpecMavlinkMsgCombinator;
    open spec fn view(&self) -> Self::V { SpecMavlinkMsgCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MavlinkMsgCombinator {
    type Type = MavlinkMsg<'a>;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MavlinkMsgCombinatorAlias = Mapped<Pair<ProtocolMagicCombinator, MavlinkMsgMsgCombinator, MavlinkMsgCont0>, MavlinkMsgMapper>;


pub open spec fn spec_mavlink_msg() -> SpecMavlinkMsgCombinator {
    SpecMavlinkMsgCombinator(
    Mapped {
        inner: Pair::spec_new(spec_protocol_magic(), |deps| spec_mavlink_msg_cont0(deps)),
        mapper: MavlinkMsgMapper,
    })
}

pub open spec fn spec_mavlink_msg_cont0(deps: SpecProtocolMagic) -> SpecMavlinkMsgMsgCombinator {
    let magic = deps;
    spec_mavlink_msg_msg(magic)
}

impl View for MavlinkMsgCont0 {
    type V = spec_fn(SpecProtocolMagic) -> SpecMavlinkMsgMsgCombinator;

    open spec fn view(&self) -> Self::V {
        |deps: SpecProtocolMagic| {
            spec_mavlink_msg_cont0(deps)
        }
    }
}


pub fn mavlink_msg<'a>() -> (o: MavlinkMsgCombinator)
    ensures o@ == spec_mavlink_msg(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MavlinkMsgCombinator(
    Mapped {
        inner: Pair::new(protocol_magic(), MavlinkMsgCont0),
        mapper: MavlinkMsgMapper,
    });
    assert({
        &&& combinator@ == spec_mavlink_msg()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_mavlink_msg<'a>(input: &'a [u8]) -> (res: PResult<<MavlinkMsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_mavlink_msg().spec_parse(input@) == Some((n as int, v@)),
        spec_mavlink_msg().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_mavlink_msg().spec_parse(input@) is None,
        spec_mavlink_msg().spec_parse(input@) is None ==> res is Err,
{
    let combinator = mavlink_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_mavlink_msg<'a>(v: <MavlinkMsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_mavlink_msg().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_mavlink_msg().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_mavlink_msg().spec_serialize(v@))
        },
{
    let combinator = mavlink_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn mavlink_msg_len<'a>(v: <MavlinkMsgCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_mavlink_msg().wf(v@),
        spec_mavlink_msg().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_mavlink_msg().spec_serialize(v@).len(),
{
    let combinator = mavlink_msg();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}

pub struct MavlinkMsgCont0;
type MavlinkMsgCont0Type<'a, 'b> = &'b ProtocolMagic;
type MavlinkMsgCont0SType<'a, 'x> = &'x ProtocolMagic;
type MavlinkMsgCont0Input<'a, 'b, 'x> = POrSType<MavlinkMsgCont0Type<'a, 'b>, MavlinkMsgCont0SType<'a, 'x>>;
impl<'a, 'b, 'x> Continuation<MavlinkMsgCont0Input<'a, 'b, 'x>> for MavlinkMsgCont0 {
    type Output = MavlinkMsgMsgCombinator;

    open spec fn requires(&self, deps: MavlinkMsgCont0Input<'a, 'b, 'x>) -> bool { true }

    open spec fn ensures(&self, deps: MavlinkMsgCont0Input<'a, 'b, 'x>, o: Self::Output) -> bool {
        o@ == spec_mavlink_msg_cont0(deps@)
    }

    fn apply(&self, deps: MavlinkMsgCont0Input<'a, 'b, 'x>) -> Self::Output {
        match deps {
            POrSType::P(deps) => {
                let magic = *deps;
                mavlink_msg_msg(magic)
            }
            POrSType::S(deps) => {
                let magic = deps;
                let magic = *magic;
                mavlink_msg_msg(magic)
            }
        }
    }
}

pub mod MavCmd {
    use super::*;
    pub spec const SPEC_FlashBootloader: u16 = 42650;
    pub exec const FlashBootloader: u16 ensures FlashBootloader == SPEC_FlashBootloader { 42650 }
}


pub struct SpecMavCmdCombinator(pub SpecMavCmdCombinatorAlias);

impl SpecCombinator for SpecMavCmdCombinator {
    type Type = u16;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecMavCmdCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecMavCmdCombinatorAlias::is_prefix_secure() }
    proof fn theorem_serialize_parse_roundtrip(&self, v: Self::Type)
    { self.0.theorem_serialize_parse_roundtrip(v) }
    proof fn theorem_parse_serialize_roundtrip(&self, buf: Seq<u8>)
    { self.0.theorem_parse_serialize_roundtrip(buf) }
    proof fn lemma_prefix_secure(&self, s1: Seq<u8>, s2: Seq<u8>)
    { self.0.lemma_prefix_secure(s1, s2) }
    proof fn lemma_parse_length(&self, s: Seq<u8>)
    { self.0.lemma_parse_length(s) }
    open spec fn is_productive(&self) -> bool
    { self.0.is_productive() }
    proof fn lemma_parse_productive(&self, s: Seq<u8>)
    { self.0.lemma_parse_productive(s) }
}
pub type SpecMavCmdCombinatorAlias = U16Le;

pub struct MavCmdCombinator(pub MavCmdCombinatorAlias);

impl View for MavCmdCombinator {
    type V = SpecMavCmdCombinator;
    open spec fn view(&self) -> Self::V { SpecMavCmdCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for MavCmdCombinator {
    type Type = u16;
    type SType = &'a Self::Type;
    fn length(&self, v: Self::SType) -> usize
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&self.0, v) }
    open spec fn ex_requires(&self) -> bool
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&self.0) }
    fn parse(&self, s: &'a [u8]) -> (res: Result<(usize, Self::Type), ParseError>)
    { <_ as Combinator<'a, &'a [u8],Vec<u8>>>::parse(&self.0, s) }
    fn serialize(&self, v: Self::SType, data: &mut Vec<u8>, pos: usize) -> (o: Result<usize, SerializeError>)
    { <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&self.0, v, data, pos) }
}
pub type MavCmdCombinatorAlias = U16Le;


pub open spec fn spec_mav_cmd() -> SpecMavCmdCombinator {
    SpecMavCmdCombinator(U16Le)
}


pub fn mav_cmd<'a>() -> (o: MavCmdCombinator)
    ensures o@ == spec_mav_cmd(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = MavCmdCombinator(U16Le);
    assert({
        &&& combinator@ == spec_mav_cmd()
        &&& combinator@.requires()
        &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    });
    combinator
}

pub fn parse_mav_cmd<'a>(input: &'a [u8]) -> (res: PResult<<MavCmdCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_mav_cmd().spec_parse(input@) == Some((n as int, v@)),
        spec_mav_cmd().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_mav_cmd().spec_parse(input@) is None,
        spec_mav_cmd().spec_parse(input@) is None ==> res is Err,
{
    let combinator = mav_cmd();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_mav_cmd<'a>(v: <MavCmdCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_mav_cmd().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_mav_cmd().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_mav_cmd().spec_serialize(v@))
        },
{
    let combinator = mav_cmd();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn mav_cmd_len<'a>(v: <MavCmdCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_mav_cmd().wf(v@),
        spec_mav_cmd().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_mav_cmd().spec_serialize(v@).len(),
{
    let combinator = mav_cmd();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}



}
