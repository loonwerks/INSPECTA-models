
#![allow(warnings)]
#![allow(unused)]
use vstd::prelude::*;
use vest_lib::regular::modifier::*;
use vest_lib::regular::bytes;
use vest_lib::regular::variant::*;
use vest_lib::regular::sequence::*;
use vest_lib::regular::repetition::*;
use vest_lib::regular::disjoint::DisjointFrom;
use vest_lib::regular::tag::*;
use vest_lib::regular::uints::*;
use vest_lib::utils::*;
use vest_lib::properties::*;
use vest_lib::bitcoin::varint::{BtcVarint, VarInt};
use vest_lib::regular::leb128::*;

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
verus!{

pub struct SpecTriple {
    pub a: u8,
    pub b: Seq<u8>,
    pub c: u64,
}

pub type SpecTripleInner = (u8, (Seq<u8>, u64));


impl SpecFrom<SpecTriple> for SpecTripleInner {
    open spec fn spec_from(m: SpecTriple) -> SpecTripleInner {
        (m.a, (m.b, m.c))
    }
}

impl SpecFrom<SpecTripleInner> for SpecTriple {
    open spec fn spec_from(m: SpecTripleInner) -> SpecTriple {
        let (a, (b, c)) = m;
        SpecTriple { a, b, c }
    }
}
#[derive(Debug, Clone, PartialEq, Eq)]

pub struct Triple<'a> {
    pub a: u8,
    pub b: &'a [u8],
    pub c: u64,
}

impl View for Triple<'_> {
    type V = SpecTriple;

    open spec fn view(&self) -> Self::V {
        SpecTriple {
            a: self.a@,
            b: self.b@,
            c: self.c@,
        }
    }
}
pub type TripleInner<'a> = (u8, (&'a [u8], u64));

pub type TripleInnerRef<'a> = (&'a u8, (&'a &'a [u8], &'a u64));
impl<'a> From<&'a Triple<'a>> for TripleInnerRef<'a> {
    fn ex_from(m: &'a Triple) -> TripleInnerRef<'a> {
        (&m.a, (&m.b, &m.c))
    }
}

impl<'a> From<TripleInner<'a>> for Triple<'a> {
    fn ex_from(m: TripleInner) -> Triple {
        let (a, (b, c)) = m;
        Triple { a, b, c }
    }
}

pub struct TripleMapper;
impl View for TripleMapper {
    type V = Self;
    open spec fn view(&self) -> Self::V {
        *self
    }
}
impl SpecIso for TripleMapper {
    type Src = SpecTripleInner;
    type Dst = SpecTriple;
}
impl SpecIsoProof for TripleMapper {
    proof fn spec_iso(s: Self::Src) {
        assert(Self::Src::spec_from(Self::Dst::spec_from(s)) == s);
    }
    proof fn spec_iso_rev(s: Self::Dst) {
        assert(Self::Dst::spec_from(Self::Src::spec_from(s)) == s);
    }
}
impl<'a> Iso<'a> for TripleMapper {
    type Src = TripleInner<'a>;
    type Dst = Triple<'a>;
    type RefSrc = TripleInnerRef<'a>;
}
pub spec const SPEC_TRIPLEB_CONST: Seq<u8> = seq![85, 119];type SpecTripleCombinatorAlias1 = (Refined<bytes::Fixed<2>, TagPred<Seq<u8>>>, U64Le);
type SpecTripleCombinatorAlias2 = (U8, SpecTripleCombinatorAlias1);
pub struct SpecTripleCombinator(pub SpecTripleCombinatorAlias);

impl SpecCombinator for SpecTripleCombinator {
    type Type = SpecTriple;
    open spec fn requires(&self) -> bool
    { self.0.requires() }
    open spec fn wf(&self, v: Self::Type) -> bool
    { self.0.wf(v) }
    open spec fn spec_parse(&self, s: Seq<u8>) -> Option<(int, Self::Type)>
    { self.0.spec_parse(s) }
    open spec fn spec_serialize(&self, v: Self::Type) -> Seq<u8>
    { self.0.spec_serialize(v) }
}
impl SecureSpecCombinator for SpecTripleCombinator {
    open spec fn is_prefix_secure() -> bool
    { SpecTripleCombinatorAlias::is_prefix_secure() }
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
pub type SpecTripleCombinatorAlias = Mapped<SpecTripleCombinatorAlias2, TripleMapper>;
pub exec static TRIPLEB_CONST: [u8; 2]
    ensures TRIPLEB_CONST@ == SPEC_TRIPLEB_CONST,
{
    let arr: [u8; 2] = [85, 119];
    assert(arr@ == SPEC_TRIPLEB_CONST);
    arr
}
type TripleCombinatorAlias1 = (Refined<bytes::Fixed<2>, TagPred<[u8; 2]>>, U64Le);
type TripleCombinatorAlias2 = (U8, TripleCombinator1);
pub struct TripleCombinator1(pub TripleCombinatorAlias1);
impl View for TripleCombinator1 {
    type V = SpecTripleCombinatorAlias1;
    open spec fn view(&self) -> Self::V { self.0@ }
}
impl_wrapper_combinator!(TripleCombinator1, TripleCombinatorAlias1);

pub struct TripleCombinator2(pub TripleCombinatorAlias2);
impl View for TripleCombinator2 {
    type V = SpecTripleCombinatorAlias2;
    open spec fn view(&self) -> Self::V { self.0@ }
}
impl_wrapper_combinator!(TripleCombinator2, TripleCombinatorAlias2);

pub struct TripleCombinator(pub TripleCombinatorAlias);

impl View for TripleCombinator {
    type V = SpecTripleCombinator;
    open spec fn view(&self) -> Self::V { SpecTripleCombinator(self.0@) }
}
impl<'a> Combinator<'a, &'a [u8], Vec<u8>> for TripleCombinator {
    type Type = Triple<'a>;
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
pub type TripleCombinatorAlias = Mapped<TripleCombinator2, TripleMapper>;


pub open spec fn spec_triple() -> SpecTripleCombinator {
    SpecTripleCombinator(
    Mapped {
        inner: (U8, (Refined { inner: bytes::Fixed::<2>, predicate: TagPred(SPEC_TRIPLEB_CONST) }, U64Le)),
        mapper: TripleMapper,
    })
}

                
pub fn triple<'a>() -> (o: TripleCombinator)
    ensures o@ == spec_triple(),
            o@.requires(),
            <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&o),
{
    let combinator = TripleCombinator(
    Mapped {
        inner: TripleCombinator2((U8, TripleCombinator1((Refined { inner: bytes::Fixed::<2>, predicate: TagPred(TRIPLEB_CONST) }, U64Le)))),
        mapper: TripleMapper,
    });
    // assert({
    //     &&& combinator@ == spec_triple()
    //     &&& combinator@.requires()
    //     &&& <_ as Combinator<'a, &'a [u8], Vec<u8>>>::ex_requires(&combinator)
    // });
    combinator
}

pub fn parse_triple<'a>(input: &'a [u8]) -> (res: PResult<<TripleCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::Type, ParseError>)
    requires
        input.len() <= usize::MAX,
    ensures
        res matches Ok((n, v)) ==> spec_triple().spec_parse(input@) == Some((n as int, v@)),
        spec_triple().spec_parse(input@) matches Some((n, v))
            ==> res matches Ok((m, u)) && m == n && v == u@,
        res is Err ==> spec_triple().spec_parse(input@) is None,
        spec_triple().spec_parse(input@) is None ==> res is Err,
{
    let combinator = triple();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::parse(&combinator, input)
}

pub fn serialize_triple<'a>(v: <TripleCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType, data: &mut Vec<u8>, pos: usize) -> (o: SResult<usize, SerializeError>)
    requires
        pos <= old(data)@.len() <= usize::MAX,
        spec_triple().wf(v@),
    ensures
        o matches Ok(n) ==> {
            &&& data@.len() == old(data)@.len()
            &&& pos <= usize::MAX - n && pos + n <= data@.len()
            &&& n == spec_triple().spec_serialize(v@).len()
            &&& data@ == seq_splice(old(data)@, pos, spec_triple().spec_serialize(v@))
        },
{
    let combinator = triple();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::serialize(&combinator, v, data, pos)
}

pub fn triple_len<'a>(v: <TripleCombinator as Combinator<'a, &'a [u8], Vec<u8>>>::SType) -> (serialize_len: usize)
    requires
        spec_triple().wf(v@),
        spec_triple().spec_serialize(v@).len() <= usize::MAX,
    ensures
        serialize_len == spec_triple().spec_serialize(v@).len(),
{
    let combinator = triple();
    <_ as Combinator<'a, &'a [u8], Vec<u8>>>::length(&combinator, v)
}

                

}
