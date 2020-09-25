package quic.invariants

object HeaderPacket {

    /** 6. Version Negotiation
      * バージョンネゴシエーションパケットの最初のバイトの最上位ビットのみが定義された値を持つ。Unused」というラベルの付いた残りの7ビットは、送信時に任意の値に設定することができ、受信時には無視されなければならない[MUST]。
      * Source Connection ID フィールドの後に、Version Negotiation パケットには、Supported Version フィールドのリストが含まれており、パケットを送信するエンドポイントがサポートしているバージョ ンを識別する。Version Negotiationパケットは他のフィールドを含まない。エンドポイントは、Supported Version フィールドを含まないパケット、または切り捨てられた Supported Version を含むパケットを無視しなければならない[MUST]。
      * Version Negotiationパケットは、完全性保護や機密性保護を使用しない。特定のQUICバージョンは、エンドポイントがサポートされているバージョンのセットの変更や破損を検出することを可能にするプロトコル要素を含むかもしれない。
      * エンドポイントは、受信したパケットの送信元接続ＩＤフィールドの値を送信先接続ＩＤフィールドに含めなければならない[MUST]。送信元接続IDの値は、クライアントによって最初にランダムに選択される受信パケットの送信先接続IDからコピーされなければならない[MUST]。両方の接続 ID をエコーすることで、サーバがパケットを受信したこと、および Version Negotiation パケットがオフパス攻撃者によって生成されたものではないことをクライアントに保証することができる。
      * Version Negotiationパケットを受信したエンドポイントは、後続のパケットに使用することを決定したバージョンを変更する可能性がある。エンドポイントがQUICのバージョンを変更する条件は、それが選択するQUICのバージョンに依存する。
      *
      * 7. Security and Privacy Considerations
      * このドキュメントで説明されているバージョンネゴシエーションパケットは完全性で保護されていない。エンドポイントは、結果として異なるQUICバージョンを試みる場合、Version Negotiationパケットの内容を認証しなければならない[MUST]。
      * 
      * @param destinationConnectionId
      * @param sourceConnectionId
      * @param versionSpecificData
      * @return
      */
    def negotiationPacket(
        destinationConnectionId: Array[Byte],
        sourceConnectionId: Array[Byte],
        versionSpecificData: Array[Byte]
    ): LongHeaderPacket = LongHeaderPacket(
        1,
        0,
        0,
        destinationConnectionId.length * 8, //FIXME real bit length
        destinationConnectionId,
        sourceConnectionId.length * 8, //FIXME real bit length
        sourceConnectionId,
        versionSpecificData
    )
}

case class LongHeaderPacket(
    headerForm: Int = 1, // 1bit
    versionSpecificBits: Int, // 7bits
    version: Int, // 32bits
    destinationConnectionIdLength: Int, // 8bits
    destinationConnectionId: Array[Byte], // 0..2040bits
    sourceConnectionIdLength: Int, // 8bits
    sourceConnectionId: Array[Byte], // 0..2040bits
    versionSpecificData: Array[Byte]
)

case class ShortHeaderPacket(
    headerForm: Int = 0, // 1bit
    versionSpecificBits: Int, // 7bits
    destinationConnectionId: Array[Byte], // 0..2040bits
    versionSpecificData: Array[Byte]
)
