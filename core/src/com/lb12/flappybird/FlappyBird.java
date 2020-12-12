package com.lb12.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch spriteBatch;
	private Texture[] zumbi;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;

	private int larguraTela = 0;
	private int alturaTela  = 0;
	private float variacao  = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;

	private float espacoEntreCanos;
	
	@Override
	public void create () {
		//Gdx.app.log( "Create", "Game initialized" );

		larguraTela = Gdx.graphics.getWidth();
		alturaTela  = Gdx.graphics.getHeight();

		posicaoInicialVertical = alturaTela / 2;
		posicaoMovimentoCanoHorizontal = larguraTela / 2;

		espacoEntreCanos = 400;

		spriteBatch = new SpriteBatch();

		canoTopo  = new Texture("cano_topo.png");
		canoBaixo = new Texture("cano_baixo.png");

		zumbi      = new Texture[ 3 ];
		zumbi[ 0 ] = new Texture( "zumbi1.png" );
		zumbi[ 1 ] = new Texture( "zumbi2.png" );
		zumbi[ 2 ] = new Texture( "zumbi3.png" );

		fundo  = new Texture( "fundo.png" );

	}

	@Override
	public void render () {
		//Gdx.app.log( "Render", "Rendering: "  );

		variacao += Gdx.graphics.getDeltaTime() * 5;
		velocidadeQueda++;

		if ( variacao > 2 )
			variacao = 0;

		if( Gdx.input.justTouched() ){
			velocidadeQueda = -20;
		}

		if ( posicaoInicialVertical > 0 || velocidadeQueda < 0 )
			posicaoInicialVertical -= velocidadeQueda;

		spriteBatch.begin();

		spriteBatch.draw( fundo, 0,0, larguraTela, alturaTela );

		spriteBatch.draw( canoTopo, posicaoMovimentoCanoHorizontal , alturaTela / 2 + espacoEntreCanos / 2 );
		spriteBatch.draw( canoBaixo, posicaoMovimentoCanoHorizontal, alturaTela / 2 - canoBaixo.getHeight() - + espacoEntreCanos / 2 );

		spriteBatch.draw( zumbi[ ( int ) variacao ], 50, posicaoInicialVertical, 300, 200 );

		spriteBatch.end();

	}

}
