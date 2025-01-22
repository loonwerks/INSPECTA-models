pub struct TransmitStatus {
    pub tx: bool,
    pub rx: bool,
}

impl TransmitStatus {
    pub fn new() -> Self {
        Self {
            tx: false,
            rx: false,
        }
    }

    pub fn any_untransmitted(&self) -> bool {
        !(self.tx && self.rx)
    }
}

impl core::ops::BitOrAssign for TransmitStatus {
    fn bitor_assign(&mut self, rhs: Self) {
        self.tx = self.tx | rhs.tx;
        self.rx = self.rx | rhs.rx;
    }
}
