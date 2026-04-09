<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="ISO-8859-1" method="html" />
	<xsl:template match="/">
		<xsl:apply-templates select="DATA" />
	</xsl:template>

	<xsl:template match="DATA">
		<html charset="UTF-8">

			<head>
				<meta http-equiv="Content-Type"
					content="text/html; charset=UTF-8" />
				<style>
					.boton-whatsapp {
					background-color: green;
					color: white;
					font-weight: 800;
					font-size: 12px;
					vertical-align: middle;
					}
					.legaltext {
					font-family: Arial;
					font-size: 9pt;
					}
					body {
					margin-left:
					auto;
					margin-right: auto;
					margin: 10px;
					max-width: 800px;
					min-width:
					600px;
					}
					p {
					text-family: Arial;
					font-size: 11pt;
					}
					.ol-text {
					text-family: Arial;
					font-size: 11pt;
					text-align: left;
					}
					.boton-deuda {
					border: 0px;
					background-color: #ea5b0c;
					color: white;
					font-weight:
					900;
					font-family: Arial;
					font-size: 16pt;
					padding-left: 1.5rem;
					padding-right: 1.5rem;
					padding-top: 1rem;
					padding-bottom: 1rem;
					}
					.text-left {
					text-align: left;
					}
					.text-center {
					text-align: center;
					}
					.w-100x {
					width: 100%;
					}

				</style>

			</head>
			<body lang="ES-AR" link="blue" vlink="purple">
				<xsl:call-template name="DEBITO" />
			</body>
		</html>
	</xsl:template>

	<xsl:template name="DEBITO">
		<table border="0" width="100%">
			<tr>
				<td style='padding:0cm 0cm 0cm 0cm'>
					<div align="center">
						<table border="0" width="600">
							<tr>
								<td style='padding:0cm 0cm 0cm 0cm'>
									<div align="center">
										<div class="text-center">
											<img src="cid:logoemail" width="80px" />
										</div>
										<div class="text-left">
											<p class="text-center">
												<b>Aviso de Deuda</b>
											</p>

											<p class="text-left">
												Buen dia
												<p class="text-left">
													Familia :
													<xsl:value-of
														select="/DATA/ROW/FAMILIA/APELLIDO" />
												</p>
												Nos ponemos en contacto para informarle
												que no hemos podido
												realizar el débito correspondiente al
												<b>mes de : </b>
												<xsl:value-of select="/DATA/ROW/MES_ACTUAL" />
												<br />

												<b>Motivo del Rechazo : </b>
												<xsl:value-of
													select="/DATA/ROW/MOTIVODELRECHAZO" />
												<br />
												Para regularizar su situación, le
												solicitamos que
												se acerque a
												la Administración en Santiago
												del Estero Nº 666
												(horario de
												atención: de 8:30 a 17:30) para
												abonar en
												efectivo o, si lo
												prefiere, realizar una
												transferencia
												bancaria y luego enviar
												el comprobante .

												Puede
												contactarnos a través del teléfono
												832050 o por mensaje de
												WhatsApp al 2954-313911.

												Agradecemos
												su colaboración.
											</p>


										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>

	</xsl:template>
</xsl:stylesheet>