package com.lb12.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	//public static float MINHA_PROPORCAO = WIDTH_IMAGEM / Gdx.graphics.getWidth();

	private SpriteBatch spriteBatch;
	private Texture[] zumbi;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Random nRandom;

	private int larguraTela  = 0;
	private int alturaTela   = 0;
	private int alturaCano   = 0;
	private int larguraCano  = 0;
	private int alturaZumbi  = 0;
	private int larguraZumbi = 0;
	private float variacao   = 0;
	private float velocidadeQueda = 0;
	private float alturaRandomica;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float deltaTime;
	private float espacoEntreCanos;

	@Override
	public void create () {
		//Gdx.app.log( "Create", "Game initialized" );

		larguraTela = Gdx.graphics.getWidth();
		alturaTela  = Gdx.graphics.getHeight();

		espacoEntreCanos = alturaTela / 5;

		alturaCano   = alturaTela / 2;
		larguraCano  = larguraTela / 6;
		alturaZumbi  = alturaTela / 20;
		larguraZumbi = larguraTela / 7;

		posicaoInicialVertical = alturaTela / 2;
		posicaoMovimentoCanoHorizontal = larguraTela + larguraCano;

		nRandom = new Random();

		spriteBatch = new SpriteBatch();

		canoTopo  = new Texture("cano_topo.png");
		canoBaixo = new Texture("cano_baixo.png");

		zumbi      = new Texture[ 3 ];
		zumbi[ 0 ] = new Texture( "zumbi1.png" );
		zumbi[ 1 ] = new Texture( "zumbi2.png" );
		zumbi[ 2 ] = new Texture( "zumbi3.png" );

		fundo = new Texture( "fundo.png" );

	}

	@Override
	public void render () {
		//Gdx.app.log( "Render", "Rendering: "  );

		deltaTime = Gdx.graphics.getDeltaTime();

		variacao += deltaTime * 5;
		posicaoMovimentoCanoHorizontal -= deltaTime * 800;
		velocidadeQueda++;

		if ( variacao > 2 )
			variacao = 0;

		if( Gdx.input.justTouched() ){
			velocidadeQueda = -23;
		}

		if ( posicaoInicialVertical > 0 || velocidadeQueda < 0 )
			posicaoInicialVertical -= velocidadeQueda;

		if ( posicaoMovimentoCanoHorizontal <= -larguraCano ){
			posicaoMovimentoCanoHorizontal = larguraTela + larguraCano;
			alturaRandomica = nRandom.nextInt( alturaCano ) - espacoEntreCanos;
		}

		spriteBatch.begin();

		spriteBatch.draw( fundo, 0,0, larguraTela, alturaTela );

		spriteBatch.draw( canoTopo, posicaoMovimentoCanoHorizontal , alturaCano + ( espacoEntreCanos / 2 ) + alturaRandomica, larguraCano, alturaCano );
		spriteBatch.draw( canoBaixo, posicaoMovimentoCanoHorizontal, -( espacoEntreCanos / 2 ) + alturaRandomica, larguraCano, alturaCano );

		spriteBatch.draw( zumbi[ ( int ) variacao ], larguraTela / 5, posicaoInicialVertical, larguraZumbi, alturaZumbi );

		spriteBatch.end();

	}

}
