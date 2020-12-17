package com.lb12.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.omg.PortableInterceptor.Interceptor;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	//public static float MINHA_PROPORCAO = WIDTH_IMAGEM / Gdx.graphics.getWidth();

	private SpriteBatch spriteBatch;
	private Texture[] zumbi;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	private Random nRandom;
	private BitmapFont fonte;

	private int larguraTela;
	private int alturaTela;
	private int alturaCano;
	private int larguraCano;
	private int alturaZumbi;
	private int larguraZumbi;
	private int posicaoZumbi;
	private float variacao  = 0;
	private float velocidadeQueda = 0;
	private float alturaRandomica;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float deltaTime;
	private float espacoEntreCanos;

	private int estadoJogo = 0;
	private int pontuacao = 0;
	private int alturaGameOver;
	private int larguraGameOver;
	private boolean marcouPonto;
	private Circle zumbiCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	//private ShapeRenderer shapeRenderer;


	@Override
	public void create () {
		//Gdx.app.log( "Create", "Game initialized" );

		larguraTela = Gdx.graphics.getWidth();
		alturaTela  = Gdx.graphics.getHeight();

		espacoEntreCanos = alturaTela / 5;

		alturaCano   = alturaTela / 2;
		larguraCano  = larguraTela / 6;

		alturaZumbi  = alturaTela / 20;
		larguraZumbi = larguraTela / 9;
		posicaoZumbi = larguraTela / 5;

		alturaGameOver  = alturaTela / 8;
		larguraGameOver = larguraTela  / 2;

		posicaoInicialVertical = alturaTela / 2;
		posicaoMovimentoCanoHorizontal = larguraTela + larguraCano;

		nRandom = new Random();

		fonte   = new BitmapFont();
		fonte.setColor( Color.WHITE );
		fonte.getData().setScale( 12 );

		spriteBatch   = new SpriteBatch();
		//shapeRenderer = new ShapeRenderer();

		zumbiCirculo       = new Circle();

		canoTopo  = new Texture("cano_topo.png");
		canoBaixo = new Texture("cano_baixo.png");

		zumbi      = new Texture[ 3 ];
		zumbi[ 0 ] = new Texture( "zumbi1.png" );
		zumbi[ 1 ] = new Texture( "zumbi2.png" );
		zumbi[ 2 ] = new Texture( "zumbi3.png" );

		fundo    = new Texture( "fundo.png"     );
		gameOver = new Texture( "game_over.png" );

	}

	@Override
	public void render () {
		//Gdx.app.log( "Render", "Rendering: "  );

		deltaTime = Gdx.graphics.getDeltaTime();

		spriteBatch.begin();
		spriteBatch.draw(fundo, 0, 0, larguraTela, alturaTela);
		spriteBatch.draw(zumbi[(int) variacao], posicaoZumbi , posicaoInicialVertical, larguraZumbi, alturaZumbi);
		spriteBatch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaCano + (espacoEntreCanos / 2) + alturaRandomica, larguraCano, alturaCano);
		spriteBatch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, -(espacoEntreCanos / 2) + alturaRandomica, larguraCano, alturaCano);

		variacao += deltaTime * 5;

		if ( variacao > 2 )
			variacao = 0;

		if ( estadoJogo != 0 ) { //Jogo sendo executado

			velocidadeQueda++;

			if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
				posicaoInicialVertical -= velocidadeQueda;

			if( estadoJogo == 1 ) {
				posicaoMovimentoCanoHorizontal -= deltaTime * 800;

				if (posicaoMovimentoCanoHorizontal <= -larguraCano) {
					posicaoMovimentoCanoHorizontal = larguraTela + larguraCano;
					alturaRandomica = nRandom.nextInt(alturaCano) - espacoEntreCanos;
					marcouPonto = false;
				}

				if (posicaoMovimentoCanoHorizontal < posicaoZumbi && !marcouPonto) {
					marcouPonto = true;
					pontuacao++;
				}

				if (Gdx.input.justTouched()) {
					velocidadeQueda = -23;
				}

			}else{ //Game Over

				spriteBatch.draw(gameOver, larguraTela / 2 - larguraGameOver / 2, alturaTela / 2, larguraGameOver , alturaGameOver );

				if( Gdx.input.justTouched() ){

					estadoJogo      = 0;
					pontuacao       = 0;
					velocidadeQueda = 0;
					posicaoInicialVertical = alturaTela / 2;
					posicaoMovimentoCanoHorizontal = larguraTela + larguraCano;

				}

			}

		}else if( Gdx.input.justTouched() ){ // Tela Inicial
			estadoJogo = 1;
			velocidadeQueda = -23;
		}

		fonte.draw( spriteBatch, String.valueOf( pontuacao ), larguraTela / 2, alturaTela - alturaTela / 12 );

		spriteBatch.end();

		zumbiCirculo.set( posicaoZumbi + larguraZumbi / 2, posicaoInicialVertical + alturaZumbi / 2, alturaZumbi / 2 );
		retanguloCanoTopo = new Rectangle( posicaoMovimentoCanoHorizontal,
									  -(espacoEntreCanos / 2) + alturaRandomica,
											larguraCano,
											alturaCano );

		retanguloCanoBaixo = new Rectangle( posicaoMovimentoCanoHorizontal,
				alturaCano + (espacoEntreCanos / 2) + alturaRandomica,
				larguraCano,
				alturaCano );

		//Desenho de formas - usado para testes de colisão
		/* shapeRenderer.begin( ShapeRenderer.ShapeType.Filled );
		shapeRenderer.circle( posicaoZumbi + larguraZumbi / 2, posicaoInicialVertical + larguraZumbi / 2, zumbiCirculo.radius );
		shapeRenderer.rect( retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height );
		shapeRenderer.rect( retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height );
		shapeRenderer.setColor(Color.BLUE);

		shapeRenderer.end(); */

		//Teste de colisão
		if( Intersector.overlaps( zumbiCirculo, retanguloCanoBaixo ) ||
			Intersector.overlaps( zumbiCirculo, retanguloCanoTopo  ) ||
			posicaoZumbi < 0 || posicaoZumbi > alturaTela            ){
			estadoJogo = 2;
		}

	}

}
